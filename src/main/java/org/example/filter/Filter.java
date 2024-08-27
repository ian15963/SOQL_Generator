package org.example.filter;

import org.example.SFHelper;
import org.example.fields.FieldOptions;

import java.lang.reflect.InvocationTargetException;
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

        private FilterBuilder() {
            stringBuilder = new StringBuilder();
        }

        private FilterBuilder(String field) {
            this.stringBuilder = new StringBuilder();
        }

        @Override
        public JoinOptions equal(Object value) {
            stringBuilder.append(" = ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions notEqual(Object value) {
            stringBuilder.append(" != ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions greaterThan(Object value) {
            stringBuilder.append(" > ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions greaterThanOrEqual(Object value) {
            stringBuilder.append(" >= ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions lessThan(Object value) {
            stringBuilder.append(" < ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions lessThanOrEqual(Object value) {
            stringBuilder.append(" <= ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
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
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
