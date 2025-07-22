package com.example.dietarysupplementshop.requests;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CreateVoucherRequest implements Serializable {
    private String couponCode;
    private BigDecimal discountValue;
    private String discountType;
    private Date expiryDate;
    private Boolean isActivated;
    private Integer remainingQuantity;
    private BigDecimal minPurchaseAmount;
    private Integer minQuantity;
    private Integer maxQuantity;
    private String description;
    private String couponType;
    private Long categoryId;
    private String shippingMethod;
    private List<Long> productIds;

    public CreateVoucherRequest() {}

    public CreateVoucherRequest(String couponCode, BigDecimal discountValue, String discountType, Date expiryDate, Boolean isActivated, Integer remainingQuantity, BigDecimal minPurchaseAmount, Integer minQuantity, Integer maxQuantity, String description, String couponType, Long categoryId, String shippingMethod, List<Long> productIds) {
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
        this.categoryId = categoryId;
        this.shippingMethod = shippingMethod;
        this.productIds = productIds;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}