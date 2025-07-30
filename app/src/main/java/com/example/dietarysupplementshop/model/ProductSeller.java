package com.example.dietarysupplementshop.model;

import java.io.Serializable;
import java.util.List;

public class ProductSeller implements Serializable {
    private String productId;
    private String productName;
    private String imageUrl;
    private List<String> media_urls;
    private double minPrice;
    private double maxPrice;
    private int stockQuantity;
    private int soldQuantity;
    private String productStatus;

    private String description;
    private String category;

    public ProductSeller(String productId, String productName, String imageUrl, List<String> media_urls,
                         double minPrice, double maxPrice, int stockQuantity, int soldQuantity, String productStatus,
                         String description, String category) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.media_urls = media_urls;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.stockQuantity = stockQuantity;
        this.soldQuantity = soldQuantity;
        this.productStatus = productStatus;
        this.description = description;
        this.category = category;
    }

    public ProductSeller(String productId, String productName, String imageUrl, double price,
                         int stockQuantity, int soldQuantity, String productStatus) {
        this(productId, productName, imageUrl, null, price, price, stockQuantity, soldQuantity, productStatus,
                "", "");
    }


    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getMedia_urls() { // Thêm getter cho media_urls
        return media_urls;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMedia_urls(List<String> media_urls) {
        this.media_urls = media_urls;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public void setProductStatus(String productStatus){
        this.productStatus = productStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}