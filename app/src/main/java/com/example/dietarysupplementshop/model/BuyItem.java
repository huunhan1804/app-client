package com.example.dietarysupplementshop.model;

public class BuyItem {
    private String productId;
    private String productVariantId;
    private int quantity;
    private double price;
    private double subTotal;

    public BuyItem(String productId, String productVariantId, int quantity, double price) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = this.price * this.quantity;
    }

    // Getters and setters for each attribute

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Update subTotal whenever quantity changes
        this.subTotal = this.price * this.quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        // Update subTotal whenever price changes
        this.subTotal = this.price * this.quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }
}
