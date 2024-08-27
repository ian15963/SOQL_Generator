package org.example.filter;

import org.example.SFHelper;
import org.example.fields.FieldOptions;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Filter{

    private String query;

    private Filter() {
    }

    private Filter(String query) {
        this.query = query;
    }

    public static class FilterBuilder implements FilterOptions, FieldOptions, JoinOptions{

        private final StringBuilder stringBuilder;
        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        private FilterBuilder() {
            stringBuilder = new StringBuilder();
        }

        private FilterBuilder(String field) {
            this.stringBuilder = new StringBuilder();
        }


        public static FilterOptions initialField(String name){
            FilterBuilder filter = new FilterBuilder();
            filter.stringBuilder.append(name);
            return filter;
        }

        @Override
        public FilterOptions field(String name) {
            stringBuilder.append("%s".formatted(name));
            return this;
        }

        @Override
        public FieldOptions and() {
            stringBuilder.append(" AND ");
            return this;
        }

        @Override
        public FieldOptions or() {
            stringBuilder.append(" OR ");
            return this;
        }

        public Filter build(){
            return new Filter(stringBuilder.toString());
        }

        @Override
        public JoinOptions subquery(Class<?> sourceClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Object value = sourceClass.getConstructor().newInstance();
            SFHelper.generateSubquery(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions equal(String value) {
            stringBuilder.append(" = \'%s\'".formatted(value));
            return this;
        }

        @Override
        public JoinOptions equal(Long value) {
            stringBuilder.append(" = %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions equal(Integer value) {
            stringBuilder.append(" = %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions equal(Double value) {
            stringBuilder.append(" = %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions equal(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" = \'%s\'".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions equal(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" = \'%s\'".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions notEqual(String value) {
            stringBuilder.append(" != %s".formatted(value));
            return this;
        }

        @Override
        public JoinOptions notEqual(Long value) {
            stringBuilder.append(" != %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions notEqual(Integer value) {
            stringBuilder.append(" != %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions notEqual(Double value) {
            stringBuilder.append(" != %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions notEqual(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" != %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions notEqual(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" != %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions greaterThan(String value) {
            stringBuilder.append(" > %s".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThan(Long value) {
            stringBuilder.append(" > %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThan(Integer value) {
            stringBuilder.append(" > %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThan(Double value) {
            stringBuilder.append(" > %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThan(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" > %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions greaterThan(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" > %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(String value) {
            stringBuilder.append(" >= %s".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(Long value) {
            stringBuilder.append(" >= %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(Integer value) {
            stringBuilder.append(" >= %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(Double value) {
            stringBuilder.append(" >= %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" >= %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" >= %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions lessThan(String value) {
            stringBuilder.append(" < %s".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThan(Long value) {
            stringBuilder.append(" < %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThan(Integer value) {
            stringBuilder.append(" < %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThan(Double value) {
            stringBuilder.append(" < %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThan(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" < %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions lessThan(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" < %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(String value) {
            stringBuilder.append(" <= %s".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(Long value) {
            stringBuilder.append(" <= %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(Integer value) {
            stringBuilder.append(" <= %d".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(Double value) {
            stringBuilder.append(" <= %f".formatted(value));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(LocalDateTime value) {
            dateTimeFormatter.format(value);
            stringBuilder.append(" <= %s".formatted(value.toString()));
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(LocalDate value) {
            dateFormatter.format(value);
            stringBuilder.append(" <= %s".formatted(value.toString()));
            return this;
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
