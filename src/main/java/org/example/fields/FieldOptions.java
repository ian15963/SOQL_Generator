package org.example.fields;

import org.example.filter.FilterOptions;

public interface FieldOptions {

    FilterOptions field(String value);
    FilterOptions closeParenthesis(String value);
}
