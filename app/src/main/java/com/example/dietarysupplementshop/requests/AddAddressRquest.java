package com.example.dietarysupplementshop.requests;

public class AddAddressRquest {
    private String fullname;
    private String phone;
    private String address_detail;

    public AddAddressRquest() {
    }

    public AddAddressRquest(String fullname, String phone, String address_detail) {
        this.fullname = fullname;
        this.phone = phone;
        this.address_detail = address_detail;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }
}
