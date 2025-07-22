package com.example.dietarysupplementshop.requests;

import java.io.Serializable;
import java.util.List;

public class ApplyCouponRequest implements Serializable {
    private String couponCode;
    private Long cartId;
    private List<Long> productIds;

    public ApplyCouponRequest(String couponCode, Long cartId) {
        this.couponCode = couponCode;
        this.cartId = cartId;
    }

    public ApplyCouponRequest(String couponCode, Long cartId, List<Long> productIds) {
        this.couponCode = couponCode;
        this.cartId = cartId;
        this.productIds = productIds;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}