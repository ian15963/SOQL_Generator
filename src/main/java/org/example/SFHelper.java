package org.example;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.example.annotation.OneToMany;
import org.example.annotation.OneToOne;
import org.example.annotation.SFColumn;
import org.example.annotation.SFEntityAnnotation;
import org.example.constant.SFType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class SFHelper {

    private static Class<?> entity;

    public static void generateBaseQuery(Class<?> classSource, StringBuilder stringBuilder) throws NoSuchFieldException {
        entity = classSource;
        Field[] fields = classSource.getDeclaredFields();
        if(!classSource.isAnnotationPresent(SFEntityAnnotation.class)){
            throw new RuntimeException();
        }
        String classAnnotationName = classSource.getDeclaredAnnotation(SFEntityAnnotation.class).name().isBlank() ? classSource.getSimpleName() : classSource.getDeclaredAnnotation(SFEntityAnnotation.class).name();
        ResultadoVerificacao resultadoVerificacao = validateTotal(classSource);
        Integer posicao = 0;
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (Field field: fields) {
            if (field.isAnnotationPresent(SFColumn.class)) {
                posicao++;
                extractField(field, stringBuilder, resultadoVerificacao, posicao, map);
            }
        }
        stringBuilder.append(" FROM ");
        stringBuilder.append(classAnnotationName);
    }

    private static void extractField(Field field, StringBuilder stringBuilder, ResultadoVerificacao resultadoVerificacao, Integer posicao, Map<String, List<String>> map){
        var isNotLast = resultadoVerificacao.isUmCampo() && posicao < resultadoVerificacao.getTotal();
        SFColumn declaredAnnotation = field.getDeclaredAnnotation(SFColumn.class);
        if(!(field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(OneToOne.class))){
            if (isNotLast){
                stringBuilder.append(" %s,".formatted(declaredAnnotation.name()));
            }else{
                stringBuilder.append(" %s".formatted(declaredAnnotation.name()));
            }
        }else{
            if(field.isAnnotationPresent(OneToMany.class) && field.isAnnotationPresent(SFColumn.class)){
                resultadoVerificacao.setPosicaoAtual(posicao);
                generateSubquery(field, stringBuilder, resultadoVerificacao);
            }else if(field.isAnnotationPresent(OneToOne.class) && field.isAnnotationPresent(SFColumn.class)){
                map.put(declaredAnnotation.name(), new ArrayList<>());
                for (Field declaredField: extractDeclaredField(field)){
                    if (declaredField.isAnnotationPresent(OneToOne.class) && declaredField.isAnnotationPresent(SFColumn.class)){
                        JavaType javaType = TypeFactory.defaultInstance().constructType(field.getType());
                        Integer posicao2 = 0;
                        extractField(declaredField, stringBuilder, validateTotal(javaType.getRawClass()), posicao2, map);
                        return;
                    }
                    if(declaredField.isAnnotationPresent(SFColumn.class)){
                        if (isNotLast){
                            map.get(declaredAnnotation.name()).add("%s,".formatted(declaredField.getDeclaredAnnotation(SFColumn.class).name()));
                        }else{
                            map.get(declaredAnnotation.name()).add("%s".formatted(declaredField.getDeclaredAnnotation(SFColumn.class).name()));
                        }
                    }
                }

                String baseEntity = "";
                for (String key: map.keySet()){
                    baseEntity = key;
                    break;
                }

                for (Map.Entry<String, List<String>> keyValue: map.entrySet()){
                    if (keyValue.getKey().equals(baseEntity)){
//                        stringBuilder.append(lista.stream().reduce(baseEntity, (a,b) -> a + "." + b));
                        for (String value: keyValue.getValue()){
                            stringBuilder.append("%s.%s".formatted(baseEntity, value));
                        }
                    }else{
                        String initial = baseEntity + "." + keyValue.getKey() + ".";

//                        stringBuilder.append(keyValue.getValue().stream().reduce(initial, (a,b) -> a + "." + b));
                        for (String value: keyValue.getValue()){
//
//                            stringBuilder.append("%s.%s.%s".formatted(baseEntity, keyValue.getKey(), value));
//                            stringBuilder.append("%s.%s.%s".formatted(baseEntity, keyValue.getKey(), value));
                        }
                    }
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

    public static void generateSubquery(Object sourceObject, StringBuilder stringBuilder, ResultadoVerificacao resultadoVerificacao){

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

        String classAnnotationName = sourceClass == Field.class ? ((Field)sourceObject).getDeclaredAnnotation(SFColumn.class).name() : sourceClass.getDeclaredAnnotation(SFEntityAnnotation.class).name();

        stringBuilder.append("(SELECT");
        Integer posicao2 = 0;
        Integer posicaoAtual = resultadoVerificacao.getPosicaoAtual();
        Map<String, List<String>> map = new LinkedHashMap<>();
        Type actualTypeArgument = sourceObject.getClass() == Field.class ? ((Field) sourceObject).getGenericType() : sourceObject.getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(actualTypeArgument);
        for (Field field1: extractDeclaredField(sourceObject)){
            if(field1.isAnnotationPresent(SFColumn.class)){
                posicao2++;
                extractField(field1, stringBuilder, validateTotal(javaType.getContentType() == null ? javaType.getRawClass() : javaType.getContentType().getRawClass()), posicao2, map);
            }
        }

        posicaoAtual = posicaoAtual == 0 ? posicao2 : posicaoAtual;

        stringBuilder.append(" FROM ");
        stringBuilder.append("%s)".formatted(classAnnotationName));

        if(posicaoAtual < resultadoVerificacao.getTotal()){
            stringBuilder.append(",");
        }

    }


    public static ResultadoVerificacao validateTotal(Class<?> sourceClass){
        int totalCamposAnotacoes = 0;
        for (Field field: sourceClass.getDeclaredFields()){
            if(field.isAnnotationPresent(SFColumn.class)){
                totalCamposAnotacoes++;
            }
        }

        return new ResultadoVerificacao(totalCamposAnotacoes, totalCamposAnotacoes > 1, 0);
    }

}



