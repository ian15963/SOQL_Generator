package org.example;

@SFEntityAnnotation(name = "Address")
public class Address {

    @SFColumn(name = "city")
    private String city;
    @SFColumn(name = "state")
    private String state;
    private String street;
}
