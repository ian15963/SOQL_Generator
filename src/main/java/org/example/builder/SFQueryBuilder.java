package org.example.builder;

import org.example.SFHelper;
import org.example.fields.FieldOptions;
import org.example.filter.Filter;

import java.util.function.Supplier;

public class SFQueryBuilder implements BuilderOptions{

    private final StringBuilder query = new StringBuilder();

    public SFQueryBuilder fromEntity(Class<?> sourceClass) throws NoSuchFieldException {
        SFHelper.generateBaseQuery(sourceClass, query);
        return this;
    }

    public SFQueryBuilder select(){
        query.append(SoqlOperators.SELECT);
        return this;
    }

    public SFQueryBuilder where(Filter filter) {
        query.append(" WHERE %s".formatted(filter.getQuery()));
        return this;
    }

    @Override
    public String build() {
        return query.toString();
    }
}


