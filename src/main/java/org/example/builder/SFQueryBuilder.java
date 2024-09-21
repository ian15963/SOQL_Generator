package org.example.builder;

import org.example.SFHelper;
import org.example.filter.Filter;

public class SFQueryBuilder implements BuilderOptions{

    private final StringBuilder query;

    private SFQueryBuilder(StringBuilder stringBuilder){
        this.query = new StringBuilder();
    }

    public static SFQueryBuilder select(Class<?> sourceClass) throws NoSuchFieldException {
        SFQueryBuilder queryBuilder = new SFQueryBuilder(new StringBuilder());
        queryBuilder.query.append("%s ".formatted(SoqlOperators.SELECT));
        SFHelper.generateBaseQuery(sourceClass, queryBuilder.query);
        return queryBuilder;
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


