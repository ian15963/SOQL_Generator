package org.example.filter;

public interface FilterOptions{

    JoinOptions equal(Object value);
    JoinOptions notEqual(Object value);

}
