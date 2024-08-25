package org.example;

import org.example.builder.SFQueryBuilder;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException {
        String query = new SFQueryBuilder().select().fromEntity(SFEntity.class).build();
        System.out.println(query);
    }
}