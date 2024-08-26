package org.example;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.constant.SFType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class SFHelper {

    public static void generateBaseQuery(Class<?> classSource, StringBuilder stringBuilder) throws NoSuchFieldException {
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
                extrairField(field, stringBuilder, resultadoVerificacao, classAnnotationName, posicao);
            }
        }
        stringBuilder.append(" FROM ");
        stringBuilder.append(classAnnotationName);
    }

    private static void extrairField(Field field, StringBuilder stringBuilder, ResultadoVerificacao resultadoVerificacao, String classAnnotationName, Integer posicao){
        var isNotLast = resultadoVerificacao.isUmCampo() && posicao < resultadoVerificacao.getTotal();
        SFColumn declaredAnnotation = field.getDeclaredAnnotation(SFColumn.class);
        if(declaredAnnotation.type().equals(SFType.SIMPLE)){
            if (isNotLast){
                stringBuilder.append(" %s.%s,".formatted(classAnnotationName,declaredAnnotation.name()));
            }else{
                stringBuilder.append("%s.%s".formatted(classAnnotationName,declaredAnnotation.name()));
            }
        }else if(declaredAnnotation.type().equals(SFType.COMPLEX)){
            if(Collection.class.isAssignableFrom(field.getType())){
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Type actualTypeArgument = genericType.getActualTypeArguments()[0];
                Field[] declaredFields = ((Class<?>) actualTypeArgument).getDeclaredFields();
                Class<?> javaType = TypeFactory.rawClass(actualTypeArgument);
                stringBuilder.append("(SELECT");
                Integer posicao2 = 0;
                for (Field field1: declaredFields){
                    if(field1.isAnnotationPresent(SFColumn.class)){
                        posicao2++;
                        extrairField(field1, stringBuilder, validarQuantidade(javaType), javaType.getSimpleName(), posicao2);
                    }
                }
                stringBuilder.append(" FROM ");
                if (isNotLast){
                    stringBuilder.append("%s.%s),".formatted(classAnnotationName,declaredAnnotation.name()));
                }else {
                    stringBuilder.append("%s.%s)".formatted(classAnnotationName,declaredAnnotation.name()));
                }
            }else{
                if (isNotLast){
                    stringBuilder.append(" %s.%s,".formatted(classAnnotationName, declaredAnnotation.name()));
                }else{
                    stringBuilder.append(" %s.%s".formatted(classAnnotationName, declaredAnnotation.name()));
                }
            }
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

    public static void generateValue(Object source, StringBuilder stringBuilder){
        Class<?> sourceClass = source.getClass();
        validarTipoClass(sourceClass, source, stringBuilder);
    }

    private static void validarTipoClass(Class<?> sourceClass, Object sourceField, StringBuilder stringBuilder){

        if(sourceClass.isAssignableFrom(String.class)){
            stringBuilder.append("\'"+ sourceField.toString() + "\'");
        }else if (Number.class.isAssignableFrom(sourceClass) || sourceClass.isAssignableFrom(Boolean.class)) {
            stringBuilder.append(sourceField.toString());
        }else{
            throw new RuntimeException();
        }
    }
}



