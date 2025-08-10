package com.example.dietarysupplementshop.responses;

public class ShopInfoDTO {
    private long shopId;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String shopEmail;
    private String shopAvatar;

    public ShopInfoDTO() {
    }

    public ShopInfoDTO(long shopId, String shopName, String shopAddress, String shopPhone, String shopEmail, String shopAvatar) {

        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopPhone = shopPhone;
        this.shopEmail = shopEmail;
        this.shopAvatar = shopAvatar;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }
}
