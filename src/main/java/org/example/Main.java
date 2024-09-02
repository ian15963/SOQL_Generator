package org.example;

import org.example.builder.SFQueryBuilder;
import org.example.filter.Filter;
import org.example.tree.BTree;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        BTree<String> bTree = new BTree<>("SFEntity");
        bTree.addRight("Address");
        bTree.addLeft("Id");
        bTree.addLeft("Name");
        bTree.addLeft("CNPJ__c");
        BTree.Node<String> stringNode = bTree.getRoot().getRight()[0];
        stringNode.setLeft(new BTree.Node[]{new BTree.Node("cep"), new BTree.Node("rua"), new BTree.Node("numero")});
        System.out.println(bTree.buildQuery());

        //        String query = SFQueryBuilder
//                .select(SFEntity.class)
//                .where(Filter.FilterBuilder
//                        .initialField("Name")
//                        .equal().subquery(new Address("São Paulo", "SP"))
//                        .and().field("Address")
//                        .equal("Haddock Lobo").and().field("Date").greaterThanOrEqual(LocalDate.now()).build()).build();
//        System.out.println(query);
    }
}