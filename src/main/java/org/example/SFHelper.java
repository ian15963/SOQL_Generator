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
        QueryControl queryControl = new QueryControl();
        for (Field field: fields) {
            if (field.isAnnotationPresent(SalesforceColumn.class)) {
                extractField(field, queryControl);
            }
        }

        buildQuery(queryControl, stringBuilder);
        stringBuilder.append(" FROM ");
        stringBuilder.append(classAnnotationName);
    }

    private static void buildQuery(QueryControl queryControl, StringBuilder stringBuilder){
        if(!queryControl.getQueryControls().isEmpty()){
            for (int i = 0; i < queryControl.getQueryControls().size(); i++){
                if (i != 0){
                    stringBuilder.append(", ");
                }
                fillQuery(queryControl.getQueryControls().get(i), stringBuilder);
            }
        }
    }

    private static void fillQuery(QueryControl queryControl, StringBuilder stringBuilder){
        if (queryControl != null){
            if (queryControl.getNome() != null){
                if (queryControl.getAnnotation() != null){
                    if (queryControl.getAnnotation().annotationType().equals(SalesforceOneToMany.class)){
                        stringBuilder.append("(SELECT ");
                        buildQuery(queryControl, stringBuilder);
                        stringBuilder.append(" FROM ");
                        stringBuilder.append("%s)".formatted(queryControl.getNome()));
                        return;
                    }
                }
                QueryControl previous = queryControl.getPrevious();
                if (queryControl.getQueryControls().isEmpty() && previous.getNome() == null){
                    stringBuilder.append("%s".formatted(queryControl.getNome()));
                }else{
                    String baseEntity = queryControl.getNome();
                    Annotation queryEntityPreviousAnnotation = queryControl.getPrevious().getAnnotation() == null ? null : queryControl.getPrevious().getAnnotation();
                    Class<?> queryEntityPreviousAnnotationType = queryEntityPreviousAnnotation == null ? null : queryEntityPreviousAnnotation.annotationType();
                    if (queryControl.getAnnotation() == null &&
                            (queryControl.getQueryControls().isEmpty() || (queryControl.getQueryControls().isEmpty() && queryEntityPreviousAnnotationType != null && queryEntityPreviousAnnotationType.equals(SalesforceOneToOne.class)))){
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
                    buildQuery(queryControl, stringBuilder);
                }
            }
        }
    }

    private static void extractField(Field field, QueryControl queryControl){
        SalesforceColumn declaredAnnotation = field.getDeclaredAnnotation(SalesforceColumn.class);
        if (queryControl.getNome() == null){
            queryControl.getQueryControls().add(new QueryControl(declaredAnnotation.name(), queryControl));
        }
        if(field.isAnnotationPresent(SalesforceOneToMany.class) && field.isAnnotationPresent(SalesforceColumn.class)){
            Type actualTypeArgument = field.getGenericType();
            JavaType javaType = TypeFactory.defaultInstance().constructType(actualTypeArgument);
            Class<?> classz = javaType.getContentType().getRawClass();
            if (classz.getDeclaredAnnotation(SalesforceEntity.class) == null){
                throw new IllegalArgumentException("Class of type %s is not a SalesforceEntity".formatted(classz.getSimpleName()));
            }
            extractQueryControl(queryControl, field, field.getDeclaredAnnotation(SalesforceOneToMany.class));
        }else if(field.isAnnotationPresent(SalesforceOneToOne.class) && field.isAnnotationPresent(SalesforceColumn.class)){
            Type actualTypeArgument = field.getGenericType();
            JavaType javaType = TypeFactory.defaultInstance().constructType(actualTypeArgument);
            Class<?> classz = javaType.getRawClass();
            if (classz.getDeclaredAnnotation(SalesforceEntity.class) == null){
                throw new IllegalArgumentException("Class of type %s is not a SalesforceEntity".formatted(classz.getSimpleName()));
            }
            extractQueryControl(queryControl, field, field.getDeclaredAnnotation(SalesforceOneToOne.class));
        }
    }

    private static void extractQueryControl(QueryControl queryControl, Field field, Annotation annotation){
        List<QueryControl> queryControlList = queryControl.getQueryControls();
        QueryControl lastValue = queryControlList.get(queryControlList.size()-1);
        lastValue.setAnnotation(annotation);
        queryControlList = lastValue.getQueryControls();
        for (Field declaredField: extractDeclaredField(field)){
            if(declaredField.isAnnotationPresent(SalesforceColumn.class)){
                queryControlList.add(new QueryControl(declaredField.getDeclaredAnnotation(SalesforceColumn.class).name(), lastValue));
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
        QueryControl queryControl = new QueryControl();
        for (Field field1: extractDeclaredField(sourceObject)){
            if(field1.isAnnotationPresent(SalesforceColumn.class)){
                extractField(field1, queryControl);
            }
        }
        buildQuery(queryControl, stringBuilder);
        stringBuilder.append(" FROM ");
        stringBuilder.append("%s)".formatted(classAnnotationName));
    }

}



