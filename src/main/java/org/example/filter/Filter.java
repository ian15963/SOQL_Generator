package org.example.filter;

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

    private static class FilterBuilder implements FilterOptions{

        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        private final StringBuilder stringBuilder;

        private String field;
        private List<String> fields;

        public FilterBuilder(String field) {
            this.stringBuilder = new StringBuilder();
            this.field = field;
            this.fields = new ArrayList<>();
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
