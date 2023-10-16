package com.example.dietarysupplementshop.model;

public class CartItem {
    private int productId;
    private String imageUrl;
    private String productName;
    private String productPrice;
    private int quantity;
    private String subTotal;

    private boolean isSelected;

    public CartItem() {
    }

    public CartItem(int productId, String imageUrl, String productName, String productPrice) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return 1;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubToal(String subTotal) {
        this.subTotal = subTotal;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
