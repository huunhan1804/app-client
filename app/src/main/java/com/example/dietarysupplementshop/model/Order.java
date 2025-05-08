package com.example.dietarysupplementshop.model;

import com.example.dietarysupplementshop.responses.OrderDetailResponse;

import java.util.Date;
import java.util.List;

public class Order {
    private long order_id;
    private Address address_info;
    private Date order_date;
    private String order_status;
    private String totalBill;

    private List<OrderDetailResponse> order_detail;

    public Order() {
    }

    public Order(long order_id, Address address_info, Date order_date, String order_status, String totalBill, List<OrderDetailResponse> order_detail) {
        this.order_id = order_id;
        this.address_info = address_info;
        this.order_date = order_date;
        this.order_status = order_status;
        this.totalBill = totalBill;
        this.order_detail = order_detail;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public Address getAddress_info() {
        return address_info;
    }

    public void setAddress_info(Address address_info) {
        this.address_info = address_info;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public List<OrderDetailResponse> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(List<OrderDetailResponse> order_detail) {
        this.order_detail = order_detail;
    }
}
