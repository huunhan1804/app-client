package com.example.dietarysupplementshop.model;

public class OrderDetail {
    private long productId;
    private long productVariantId;
    private int quantity;
    private String price;
    private String subtotal;

    public OrderDetail() {
    }

    public OrderDetail(long productId, long productVariantId, int quantity, String price, String subtotal) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(long productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
