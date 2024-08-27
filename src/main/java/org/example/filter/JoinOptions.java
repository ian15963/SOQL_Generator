package org.example.filter;

import org.example.fields.FieldOptions;

import java.lang.reflect.InvocationTargetException;

public interface JoinOptions {

    FieldOptions and();
    FieldOptions or();
    Filter build();
}
