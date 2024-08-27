package org.example;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.constant.SFType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class SFHelper {

    private static Class<?> entity;

    public static void generateBaseQuery(Class<?> classSource, StringBuilder stringBuilder) throws NoSuchFieldException {
        entity = classSource;
        Field[] fields = classSource.getDeclaredFields();
        if(!classSource.isAnnotationPresent(SFEntityAnnotation.class)){
            throw new RuntimeException();
        }
        String classAnnotationName = classSource.getDeclaredAnnotation(SFEntityAnnotation.class).name().isBlank() ? classSource.getSimpleName() : classSource.getDeclaredAnnotation(SFEntityAnnotation.class).name();
        ResultadoVerificacao resultadoVerificacao = validarQuantidade(classSource);
        Integer posicao = 0;
        for (Field field: fields) {
            if (field.isAnnotationPresent(SFColumn.class)) {
                posicao++;
                extractField(field, stringBuilder, resultadoVerificacao, posicao);
            }
        }
        stringBuilder.append(" FROM ");
        stringBuilder.append(classAnnotationName);
    }

    private static void extractField(Field field, StringBuilder stringBuilder, ResultadoVerificacao resultadoVerificacao, Integer posicao){
        var isNotLast = resultadoVerificacao.isUmCampo() && posicao < resultadoVerificacao.getTotal();
        SFColumn declaredAnnotation = field.getDeclaredAnnotation(SFColumn.class);
        if(declaredAnnotation.type().equals(SFType.SIMPLE)){
            if (isNotLast){
                stringBuilder.append(" %s,".formatted(declaredAnnotation.name()));
            }else{
                stringBuilder.append(" %s".formatted(declaredAnnotation.name()));
            }
        }else if(declaredAnnotation.type().equals(SFType.COMPLEX)){
            if(Collection.class.isAssignableFrom(field.getType())){
                generateSubquery(field, stringBuilder);
            }else{
                for (Field declaredField: extractDeclaredField(field)){
                    if (isNotLast){
                        stringBuilder.append(" %s.%s,".formatted(declaredAnnotation.name(), declaredField.getName()));
                    }else{
                        stringBuilder.append(" %s.%s".formatted(declaredAnnotation.name(), declaredField.getName()));
                    }
                }
            }
        }
    }

    private static Field[] extractDeclaredField(Object sourceObject){
        Type actualTypeArgument = ((Field) sourceObject).getGenericType();
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

        if(sourceClass.isAssignableFrom(String.class)){
            stringBuilder.append(sourceObject);
            return;
        }

        Integer totalInvalido = 0;
        Field[] fields = entity.getDeclaredFields();

        for (Field field: fields){
            if(!field.getType().equals(sourceClass)){
                totalInvalido++;
            }
        }

//        if(totalInvalido == fields.length){
//            throw new RuntimeException();
//        }

        Type actualTypeArgument = ((Field) sourceObject).getGenericType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(actualTypeArgument);
        Class<?> actualClass;
        if(javaType.getContentType() != null){
            actualClass = javaType.getContentType().getRawClass();
        }else{
            actualClass = javaType.getRawClass();
        }

        String classAnnotationName = ((Field)sourceObject).getDeclaredAnnotation(SFColumn.class).name();

        stringBuilder.append("(SELECT");
        Integer posicao2 = 0;
        for (Field field1: extractDeclaredField(sourceObject)){
            if(field1.isAnnotationPresent(SFColumn.class)){
                posicao2++;
                extractField(field1, stringBuilder, validarQuantidade(actualClass), posicao2);
            }
        }
        stringBuilder.append(" FROM ");
        if (validarQuantidade(actualClass).isUmCampo()){
            stringBuilder.append("%s),".formatted(classAnnotationName));
        }else {
            stringBuilder.append("%s)".formatted(classAnnotationName));
        }
    }


    private static ResultadoVerificacao validarQuantidade(Class<?> sourceClass){
        int totalCamposAnotacoes = 0;
        for (Field field: sourceClass.getDeclaredFields()){
            if(field.isAnnotationPresent(SFColumn.class)){
                totalCamposAnotacoes++;
            }
        }

        return new ResultadoVerificacao(totalCamposAnotacoes, totalCamposAnotacoes > 1);
    }

}



