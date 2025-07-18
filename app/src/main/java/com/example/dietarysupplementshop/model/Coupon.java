package com.example.dietarysupplementshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Coupon implements Serializable {
    private Long couponId;
    private Long categoryId;
    private String couponCode;
    private BigDecimal discountValue;
    private String discountType; // ENUM: 'AMOUNT','PERCENTAGE'
    private Date expiryDate;
    private Boolean isActivated;
    private Integer remainingQuantity;
    private BigDecimal minPurchaseAmount;
    private Integer minQuantity;
    private Integer maxQuantity;
    private String description;
    private String couponType; // ENUM: 'SHIPPING','DISCOUNT'
    private boolean isSelected;

    public Coupon() {}



    // Constructor bạn đang cố gắng sử dụng trong MockCouponRepository
    public Coupon(String couponCode, String description, double discountValue, String discountType) {
        this.couponCode = couponCode;
        this.description = description;
        this.discountValue = BigDecimal.valueOf(discountValue);
        this.discountType = discountType;
        this.couponId = null;
        this.categoryId = null;
        this.expiryDate = null;
        this.isActivated = true;
        this.remainingQuantity = null;
        this.minPurchaseAmount = BigDecimal.ZERO;
        this.minQuantity = null;
        this.maxQuantity = null;
        this.couponType = null;
    }


    public Coupon(Long couponId, Long categoryId, String couponCode, BigDecimal discountValue, String discountType, Date expiryDate, Boolean isActivated, Integer remainingQuantity, BigDecimal minPurchaseAmount, Integer minQuantity, Integer maxQuantity, String description, String couponType) {
        this.couponId = couponId;
        this.categoryId = categoryId;
        this.couponCode = couponCode;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.expiryDate = expiryDate;
        this.isActivated = isActivated;
        this.remainingQuantity = remainingQuantity;
        this.minPurchaseAmount = minPurchaseAmount;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.description = description;
        this.couponType = couponType;
        this.isSelected = false;

    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public BigDecimal getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUsable() {
        return isActivated != null && isActivated &&
                (remainingQuantity == null || remainingQuantity > 0) &&
                (expiryDate == null || expiryDate.after(new Date()));
    }
}