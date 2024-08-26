package org.example;

import org.example.constant.SFType;

import java.util.ArrayList;
import java.util.List;

@SFEntityAnnotation(name = "Entity")
public class SFEntity {

    @SFColumn(name = "Id")
    private String id;

    @SFColumn(name = "Name")
    private String name;
    @SFColumn(name = "Address", type = SFType.COMPLEX)
    private Address address;

    @SFColumn(name = "Addresses", type = SFType.COMPLEX)
    private List<Address> valores = new ArrayList<>();

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
