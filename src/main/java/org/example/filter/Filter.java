package org.example.filter;

import com.fasterxml.jackson.databind.JavaType;
import org.example.SFHelper;
import org.example.fields.FieldOptions;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Filter{

    private String query;

    private Filter() {
    }

    private Filter(String query) {
        this.query = query;
    }

    public static class FilterBuilder implements FilterOptions, FieldOptions, JoinOptions{

        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        private final StringBuilder stringBuilder;

        private String field;
        private List<String> fields;

        public FilterBuilder() {
            stringBuilder = new StringBuilder();
            fields = new ArrayList<>();
        }

        public FilterBuilder(String field) {
            this.stringBuilder = new StringBuilder();
            this.field = field;
            this.fields = new ArrayList<>();
        }

        @Override
        public FilterBuilder equal(Object value) {
            stringBuilder.append(" = ");
            SFHelper.generateValue(value, stringBuilder);
            return this;
        }

        @Override
        public JoinOptions notEqual(Object value) {
            return null;
        }

        @Override
        public FilterOptions field(String name) {
            stringBuilder.append("%s".formatted(name));
            return this;
        }

        @Override
        public FieldOptions and() {
            return null;
        }

        @Override
        public FieldOptions or() {
            return null;
        }

        public Filter build(){
            return new Filter(stringBuilder.toString());
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
