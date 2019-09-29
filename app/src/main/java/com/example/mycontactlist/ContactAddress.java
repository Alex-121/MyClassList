package com.example.mycontactlist;

public class ContactAddress {
    String street;
    String state;
    String zipcode;
    String city;
    int contactId;

    ContactAddress(Contact c) {
        this.contactId = c.getContactID();
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getContactID() {
        return contactId;
    }
}
