package org.example;

import org.example.annotation.SalesforceOneToOne;
import org.example.annotation.SalesforceColumn;
import org.example.annotation.SalesforceEntity;

@SalesforceEntity(name = "Address")
public class Address {

    @SalesforceColumn(name = "city")
    private String city;
    @SalesforceColumn(name = "state")
    private String state;

    @SalesforceColumn(name = "lead")
    @SalesforceOneToOne
    private Lead lead;
    private String street;

    public Address() {
    }

    public Address(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public Address(String city, String state, String street) {
        this.city = city;
        this.state = state;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}
