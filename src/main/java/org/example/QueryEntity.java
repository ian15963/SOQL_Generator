package org.example;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class QueryEntity {

    private QueryEntity previous;
    private String nome;
    private List<QueryEntity> queryEntities;
    private Annotation annotation;

    public QueryEntity() {
        queryEntities = new ArrayList<>();
    }

    public QueryEntity(String nome, QueryEntity previous) {
        this.nome = nome;
        this.previous = previous;
        this.queryEntities = new ArrayList<>();
    }

    public QueryEntity(String nome, QueryEntity previous,Annotation annotation) {
        this(nome, previous);
        this.annotation = annotation;
        this.queryEntities = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<QueryEntity> getQueryEntities() {
        return queryEntities;
    }

    public void setQueryEntities(List<QueryEntity> queryEntities) {
        this.queryEntities = queryEntities;
    }

    public QueryEntity getPrevious() {
        return previous;
    }

    public void setPrevious(QueryEntity previous) {
        this.previous = previous;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
