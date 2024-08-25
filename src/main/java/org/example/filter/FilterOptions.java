package org.example.filter;

import org.example.fields.FieldOptions;

public interface FilterOptions{

    FieldOptions where(String value);
    JoinOptions equal(String value);

}
