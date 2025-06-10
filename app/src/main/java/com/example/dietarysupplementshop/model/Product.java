package com.example.dietarysupplementshop.model;

public class Product {
    private long product_id;
    private String image_url;
    private String product_name;
    private String product_price;
    private double rating;

    public Product() {
    }

    public Product(long product_id, String image_url, String product_name, String product_price, double rating) {
        this.product_id = product_id;
        this.image_url = image_url;
        this.product_name = product_name;
        this.product_price = product_price;
        this.rating = rating;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

