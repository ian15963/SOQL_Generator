package org.example.filter;

import org.example.fields.FieldOptions;

import java.lang.reflect.InvocationTargetException;

public interface SubqueryOptions {

    JoinOptions subquery(Object source) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
    FieldOptions openParenthesis(String value);

}
