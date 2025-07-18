package com.example.dietarysupplementshop.model;

public class Shop {
    private long shopId;
    private String shopName;
    private String avatarUrl;

    public Shop() {
    }

    public Shop(long shopId, String shopName, String avatarUrl) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.avatarUrl = avatarUrl;
    }

    public long getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}