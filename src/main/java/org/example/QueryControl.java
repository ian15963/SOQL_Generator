package org.example;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class QueryControl {

    private QueryControl previous;
    private String nome;
    private List<QueryControl> queryControls;
    private Annotation annotation;

    public QueryControl() {
        queryControls = new ArrayList<>();
    }

    public QueryControl(String nome, QueryControl previous) {
        this.nome = nome;
        this.previous = previous;
        this.queryControls = new ArrayList<>();
    }

    public QueryControl(String nome, QueryControl previous, Annotation annotation) {
        this(nome, previous);
        this.annotation = annotation;
        this.queryControls = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<QueryControl> getQueryControls() {
        return queryControls;
    }

    public void setQueryControls(List<QueryControl> queryControls) {
        this.queryControls = queryControls;
    }

    public QueryControl getPrevious() {
        return previous;
    }

    public void setPrevious(QueryControl previous) {
        this.previous = previous;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
