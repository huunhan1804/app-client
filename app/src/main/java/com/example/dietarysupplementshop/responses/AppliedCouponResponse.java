package com.example.dietarysupplementshop.responses;

import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.model.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class AppliedCouponResponse implements Serializable {
    private Coupon appliedCoupon;
    private BigDecimal originalTotalPrice;
    private BigDecimal discountAmount;
    private BigDecimal finalTotalPrice;
    private String message;
    private List<CartItem> updatedCartItems;
    private boolean success;

    public AppliedCouponResponse() {}
    public AppliedCouponResponse(Coupon appliedCoupon, BigDecimal originalTotalPrice, BigDecimal discountAmount, BigDecimal finalTotalPrice, String message, List<CartItem> updatedCartItems) {
        this.appliedCoupon = appliedCoupon;
        this.originalTotalPrice = originalTotalPrice;
        this.discountAmount = discountAmount;
        this.finalTotalPrice = finalTotalPrice;
        this.message = message;
        this.updatedCartItems = updatedCartItems;
        this.success = true;

    }

    // Constructor mới để hỗ trợ MockCouponRepository
    public AppliedCouponResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.appliedCoupon = null;
        this.originalTotalPrice = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.finalTotalPrice = BigDecimal.ZERO;
        this.updatedCartItems = null;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(Coupon appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    public BigDecimal getOriginalTotalPrice() {
        return originalTotalPrice;
    }

    public void setOriginalTotalPrice(BigDecimal originalTotalPrice) {
        this.originalTotalPrice = originalTotalPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalTotalPrice() {
        return finalTotalPrice;
    }

    public void setFinalTotalPrice(BigDecimal finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CartItem> getUpdatedCartItems() {
        return updatedCartItems;
    }

    public void setUpdatedCartItems(List<CartItem> updatedCartItems) {
        this.updatedCartItems = updatedCartItems;
    }
}