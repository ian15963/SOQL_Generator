package org.example.filter;

public interface FilterOptions{

    JoinOptions equal(Object value);
    JoinOptions notEqual(Object value);
    JoinOptions greaterThan(Object value);
    JoinOptions greaterThanOrEqual(Object value);
    JoinOptions lessThan(Object value);
    JoinOptions lessThanOrEqual(Object value);

}
