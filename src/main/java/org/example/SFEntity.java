package org.example;

import org.example.annotation.SalesforceOneToMany;
import org.example.annotation.SalesforceOneToOne;
import org.example.annotation.SalesforceColumn;
import org.example.annotation.SalesforceEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SalesforceEntity
public class SFEntity {

    @SalesforceColumn(name = "Id")
    private String id;

    @SalesforceColumn(name = "Name")
    private String name;

    @SalesforceColumn(name = "Address")
    @SalesforceOneToOne
    private Address address;

    @SalesforceColumn(name = "Addresses")
    @SalesforceOneToMany
    private List<Address> valores = new ArrayList<>();

    @SalesforceColumn(name = "Date")
    private LocalDateTime dataHora;

    public SFEntity() {
    }

    public SFEntity(String id, String name, Address address, List<Address> valores) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.valores = valores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getValores() {
        return valores;
    }

    public void setValores(List<Address> valores) {
        this.valores = valores;
    }
}
