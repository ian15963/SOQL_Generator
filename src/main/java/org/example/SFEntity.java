package org.example;

import org.example.annotation.OneToMany;
import org.example.annotation.OneToOne;
import org.example.annotation.SFColumn;
import org.example.annotation.SFEntityAnnotation;
import org.example.constant.SFType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SFEntityAnnotation
public class SFEntity {

    @SFColumn(name = "Id")
    private String id;

    @SFColumn(name = "Name")
    private String name;

    @SFColumn(name = "Address")
    @OneToOne
    private Address address;

    @SFColumn(name = "Addresses")
    @OneToMany
    private List<Address> valores = new ArrayList<>();

    @SFColumn(name = "Date")
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
