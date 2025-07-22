package com.example.dietarysupplementshop.responses;

import com.example.dietarysupplementshop.model.Coupon;
import java.io.Serializable;

public class VoucherCreationResponse implements Serializable {
    private String status;
    private String message;
    private Coupon createdCoupon;

    public VoucherCreationResponse() {}

    public VoucherCreationResponse(String status, String message, Coupon createdCoupon) {
        this.status = status;
        this.message = message;
        this.createdCoupon = createdCoupon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Coupon getCreatedCoupon() {
        return createdCoupon;
    }

    public void setCreatedCoupon(Coupon createdCoupon) {
        this.createdCoupon = createdCoupon;
    }
}