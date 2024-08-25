package org.example;

import org.example.builder.SFQueryBuilder;
import org.example.filter.Filter;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException {
        String query = new SFQueryBuilder().select().fromEntity(SFEntity.class).where(new Filter.FilterBuilder().field("Name").equal(1).build()).build();
        System.out.println(query);
    }
}