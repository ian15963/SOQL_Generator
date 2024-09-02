package org.example;

import org.example.annotation.OneToOne;
import org.example.annotation.SFColumn;
import org.example.annotation.SFEntityAnnotation;

@SFEntityAnnotation
public class Lead {

    @SFColumn(name = "Id")
    private Long id;
    @SFColumn(name = "Name__c")
    private String name;
    @SFColumn(name = "contact")
    @OneToOne
    private Contact contact;

    private static class Contact{

        @SFColumn(name = "phone")
        private String phone;
    }

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
