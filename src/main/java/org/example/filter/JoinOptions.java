package org.example.filter;

import org.example.fields.FieldOptions;

public interface JoinOptions {

    FieldOptions and();
    FieldOptions or();
    Filter build();
}
