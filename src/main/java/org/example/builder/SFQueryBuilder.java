package org.example.builder;

import org.example.SFHelper;
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

    public SFQueryBuilder where(Supplier<String> supplier) {
        supplier.get();
        query.append(" WHERE ");
        return this;
    }

    @Override
    public String build() {
        return query.toString();
    }
}


