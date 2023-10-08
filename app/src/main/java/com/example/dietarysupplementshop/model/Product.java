package com.example.dietarysupplementshop.model;

public class Product {
    private int productId;
    private String imageUrl;
    private String productName;
    private String productPrice;
    private int rating;

    public Product() {
    }

    public Product(int productId, String imageUrl, String productName, String productPrice, int rating) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

