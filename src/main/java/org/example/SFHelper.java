package org.example;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.annotation.SalesforceOneToMany;
import org.example.annotation.SalesforceOneToOne;
import org.example.annotation.SalesforceColumn;
import org.example.annotation.SalesforceEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class SFHelper {

    public static void generateBaseQuery(Class<?> classSource, StringBuilder stringBuilder) throws NoSuchFieldException {
        Field[] fields = classSource.getDeclaredFields();
        if(!classSource.isAnnotationPresent(SalesforceEntity.class)){
            throw new RuntimeException();
        }
        String classAnnotationName = classSource.getDeclaredAnnotation(SalesforceEntity.class).name().isBlank() ? classSource.getSimpleName() : classSource.getDeclaredAnnotation(SalesforceEntity.class).name();
        QueryEntity queryEntity = new QueryEntity();
        for (Field field: fields) {
            if (field.isAnnotationPresent(SalesforceColumn.class)) {
                extractField(field, queryEntity);
            }
        }

        buildQuery(queryEntity, stringBuilder);
        stringBuilder.append(" FROM ");
        stringBuilder.append(classAnnotationName);
    }

    public static void buildQuery(QueryEntity queryEntity, StringBuilder stringBuilder){
        if(!queryEntity.getQueryEntities().isEmpty()){
            for (int i = 0; i < queryEntity.getQueryEntities().size(); i++){
                if (i != 0){
                    stringBuilder.append(", ");
                }
                fillQuery(queryEntity.getQueryEntities().get(i), stringBuilder);
            }
        }
    }

    public static void fillQuery(QueryEntity queryEntity, StringBuilder stringBuilder){
        if (queryEntity != null){
            if (queryEntity.getNome() != null){
                if (queryEntity.getAnnotation() != null){
                    if (queryEntity.getAnnotation().annotationType().equals(SalesforceOneToMany.class)){
                        stringBuilder.append("(SELECT ");
                        buildQuery(queryEntity, stringBuilder);
                        stringBuilder.append(" FROM ");
                        stringBuilder.append("%s)".formatted(queryEntity.getNome()));
                        return;
                    }
                }
                QueryEntity previous = queryEntity.getPrevious();
                if (queryEntity.getQueryEntities().isEmpty() && previous.getNome() == null){
                    stringBuilder.append("%s".formatted(queryEntity.getNome()));
                }else{
                    String baseEntity = queryEntity.getNome();
                    Annotation queryEntityPreviousAnnotation = queryEntity.getPrevious().getAnnotation() == null ? null : queryEntity.getPrevious().getAnnotation();
                    Class<?> queryEntityPreviousAnnotationType = queryEntityPreviousAnnotation == null ? null : queryEntityPreviousAnnotation.annotationType();
                    if (queryEntity.getAnnotation() == null &&
                            (queryEntity.getQueryEntities().isEmpty() || (queryEntity.getQueryEntities().isEmpty() && queryEntityPreviousAnnotationType != null && queryEntityPreviousAnnotationType.equals(SalesforceOneToOne.class)))){
                        if (previous.getNome() != null){
                            while(previous != null){
                                if (previous.getNome() != null){
                                    baseEntity = previous.getNome() + "." + baseEntity;
                                }
                                previous = previous.getPrevious();
                            }
                            stringBuilder.append("%s".formatted(baseEntity));
                        }
                    }
                    buildQuery(queryEntity, stringBuilder);
                }
            }
        }
    }

    private static void extractField(Field field, QueryEntity queryEntity){
        SalesforceColumn declaredAnnotation = field.getDeclaredAnnotation(SalesforceColumn.class);
        if (queryEntity.getNome() == null){
            queryEntity.getQueryEntities().add(new QueryEntity(declaredAnnotation.name(), queryEntity));
        }

        if(field.isAnnotationPresent(SalesforceOneToMany.class) && field.isAnnotationPresent(SalesforceColumn.class)){
           extractQueryEntity(queryEntity, field, field.getDeclaredAnnotation(SalesforceOneToMany.class));
        }else if(field.isAnnotationPresent(SalesforceOneToOne.class) && field.isAnnotationPresent(SalesforceColumn.class)){
           extractQueryEntity(queryEntity, field, field.getDeclaredAnnotation(SalesforceOneToOne.class));
        }
    }

    private static void extractQueryEntity(QueryEntity queryEntity, Field field, Annotation annotation){
        List<QueryEntity> queryEntityList = queryEntity.getQueryEntities();
        QueryEntity lastValue = queryEntityList.get(queryEntityList.size()-1);
        lastValue.setAnnotation(annotation);
        queryEntityList = lastValue.getQueryEntities();
        for (Field declaredField: extractDeclaredField(field)){
            if(declaredField.isAnnotationPresent(SalesforceColumn.class)){
                queryEntityList.add(new QueryEntity(declaredField.getDeclaredAnnotation(SalesforceColumn.class).name(), lastValue));
                if (declaredField.isAnnotationPresent(SalesforceOneToOne.class) || declaredField.isAnnotationPresent(SalesforceOneToMany.class)){
                    extractField(declaredField, lastValue);
                }
            }
        }
    }

    private static Field[] extractDeclaredField(Object sourceObject){
        Type actualTypeArgument = sourceObject.getClass() == Field.class ? ((Field) sourceObject).getGenericType() : sourceObject.getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(actualTypeArgument);

        Field[] declaredFields;

        if(javaType.getContentType() != null){
            declaredFields = javaType.getContentType().getRawClass().getDeclaredFields();
        }else{
            declaredFields = javaType.getRawClass().getDeclaredFields();
        }

        return declaredFields;
    }

    public static void generateSubquery(Object sourceObject, StringBuilder stringBuilder){

        Class<?> sourceClass = sourceObject.getClass();
        String classAnnotationName = sourceClass == Field.class ? ((Field)sourceObject).getDeclaredAnnotation(SalesforceColumn.class).name() : sourceClass.getDeclaredAnnotation(SalesforceEntity.class).name();

        stringBuilder.append("(SELECT ");
        QueryEntity queryEntity = new QueryEntity();
        for (Field field1: extractDeclaredField(sourceObject)){
            if(field1.isAnnotationPresent(SalesforceColumn.class)){
                extractField(field1, queryEntity);
            }
        }
        buildQuery(queryEntity, stringBuilder);
        stringBuilder.append(" FROM ");
        stringBuilder.append("%s)".formatted(classAnnotationName));
    }

}



