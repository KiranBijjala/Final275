package edu.sjsu.cmpe275.lab2.Lab2.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Embeddable;
import javax.persistence.*;

@Embeddable
public class Address {

    private String street;

    private String city;

    private String state;

    private String zip;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}