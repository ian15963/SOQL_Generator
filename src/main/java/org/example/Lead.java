package org.example;

import org.example.annotation.SalesforceColumn;
import org.example.annotation.SalesforceEntity;

@SalesforceEntity
public class Lead {

    @SalesforceColumn(name = "Id")
    private Long id;
    @SalesforceColumn(name = "Name__c")
    private String name;

    public Lead() {
    }

    public Lead(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
