package org.example;

import org.example.builder.SFQueryBuilder;
import org.example.filter.Filter;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException {
        String query = SFQueryBuilder.select(SFEntity.class)
                .where(Filter.FilterBuilder
                        .initialField("Name")
                        .equal("Ian")
                        .and().field("Address")
                        .equal("Haddock Lobo").build()).build();
        System.out.println(query);
    }
}