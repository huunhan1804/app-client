package com.example.dietarysupplementshop.requests;

import com.example.dietarysupplementshop.model.OrderDetail;

import java.util.List;

public class OrderRequest {
    private String address_detail;
    private String total_bill;
    private List<OrderDetail> order_items;

    public OrderRequest() {
    }

    public OrderRequest(String address_detail, String total_bill, List<OrderDetail> order_items) {
        this.address_detail = address_detail;
        this.total_bill = total_bill;
        this.order_items = order_items;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(String total_bill) {
        this.total_bill = total_bill;
    }

    public List<OrderDetail> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderDetail> order_items) {
        this.order_items = order_items;
    }
}
