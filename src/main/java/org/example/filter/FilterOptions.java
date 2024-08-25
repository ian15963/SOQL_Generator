package org.example.filter;

import org.example.fields.FieldOptions;

public interface FilterOptions{

    Filter.FilterBuilder equal(Object value);
    JoinOptions notEqual(Object value);

}
