package com.example.dietarysupplementshop.model;

import java.io.Serializable;

public class AccountCoupon implements Serializable {
    private Coupon coupon;
    private String status; // USABLE, USED, EXPIRED - THÊM TRƯỜNG NÀY VÀO LỚP ACCOUNTCOUPON
    private String startDate;
    private String endDate;

    // Constructors
    public AccountCoupon() {}

    public AccountCoupon(Coupon coupon, String status, String startDate, String endDate) {
        this.coupon = coupon;
        this.status = status; // Khởi tạo trường status
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}