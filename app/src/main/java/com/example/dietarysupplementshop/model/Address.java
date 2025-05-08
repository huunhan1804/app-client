package com.example.dietarysupplementshop.model;

import java.math.BigInteger;

public class Address {
    private long address_id;
    private String fullname;
    private String phone;
    private String address_detail;
    private boolean _default;

    public Address() {
    }

    public Address(long address_id, String fullname, String phone, String address_detail, boolean is_default) {
        this.address_id = address_id;
        this.fullname = fullname;
        this.phone = phone;
        this.address_detail = address_detail;
        this._default = is_default;
    }

    public long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(long address_id) {
        this.address_id = address_id;
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

    public boolean getIs_default() {
        return _default;
    }

    public void setIs_default(boolean is_default) {
        this._default = is_default;
    }
}
