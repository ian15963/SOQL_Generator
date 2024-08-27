package org.example;

import org.example.builder.SFQueryBuilder;
import org.example.filter.Filter;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String query = SFQueryBuilder.select(SFEntity.class)
                .where(Filter.FilterBuilder
                        .initialField("Name")
                        .equal("Ian")
//                        .subquery(Address.class)
                        .and().field("Address")
                        .equal("Haddock Lobo").and().field("Date").greaterThanOrEqual(LocalDate.now()).build()).build();
        System.out.println(query);
    }
}