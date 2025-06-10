package com.example.dietarysupplementshop.requests;

public class UpdateAddressRequest {
    private Long address_id;
    private String fullname;
    private String phone;
    private String address_detail;

    public UpdateAddressRequest() {
    }

    public UpdateAddressRequest(Long address_id, String fullname, String phone, String address_detail) {
        this.address_id = address_id;
        this.fullname = fullname;
        this.phone = phone;
        this.address_detail = address_detail;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
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
}
