package org.example.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FilterOptions{

    JoinOptions equal(String value);
    JoinOptions equal(Long value);
    JoinOptions equal(Integer value);
    JoinOptions equal(Double value);
    JoinOptions equal(LocalDateTime value);
    JoinOptions equal(LocalDate value);
    SubqueryOptions equal();
    JoinOptions notEqual(String value);
    JoinOptions notEqual(Long value);
    JoinOptions notEqual(Integer value);
    JoinOptions notEqual(Double value);
    JoinOptions notEqual(LocalDateTime value);
    JoinOptions notEqual(LocalDate value);
    JoinOptions greaterThan(String value);
    JoinOptions greaterThan(Long value);
    JoinOptions greaterThan(Integer value);
    JoinOptions greaterThan(Double value);
    JoinOptions greaterThan(LocalDateTime value);
    JoinOptions greaterThan(LocalDate value);
    JoinOptions greaterThanOrEqual(String value);
    JoinOptions greaterThanOrEqual(Long value);
    JoinOptions greaterThanOrEqual(Integer value);
    JoinOptions greaterThanOrEqual(Double value);
    JoinOptions greaterThanOrEqual(LocalDateTime value);
    JoinOptions greaterThanOrEqual(LocalDate value);
    JoinOptions lessThan(String value);
    JoinOptions lessThan(Long value);
    JoinOptions lessThan(Integer value);
    JoinOptions lessThan(Double value);
    JoinOptions lessThan(LocalDateTime value);
    JoinOptions lessThan(LocalDate value);
    JoinOptions lessThanOrEqual(String value);
    JoinOptions lessThanOrEqual(Long value);
    JoinOptions lessThanOrEqual(Integer value);
    JoinOptions lessThanOrEqual(Double value);
    JoinOptions lessThanOrEqual(LocalDateTime value);
    JoinOptions lessThanOrEqual(LocalDate value);

}
