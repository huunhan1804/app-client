package com.example.dietarysupplementshop.responses;

import java.util.List;

public class ProductInformation {
    private Long product_id;
    private String product_name;
    private String product_price;
    private String product_description;
    private double rating;
    private int quantity_in_stock;
    private List<String> media_url;
    private List<ProductVariantDTO> product_variant_list;
    private List<FeedbackDTO> feedback_list;

    public ProductInformation(Long product_id, String product_name, String product_price, String product_description, double rating, int quantity_in_stock, List<String> media_url, List<ProductVariantDTO> product_variant_list, List<FeedbackDTO> feedback_list) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_description = product_description;
        this.rating = rating;
        this.quantity_in_stock = quantity_in_stock;
        this.media_url = media_url;
        this.product_variant_list = product_variant_list;
        this.feedback_list = feedback_list;
    }

    public ProductInformation() {
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public List<String> getMedia_url() {
        return media_url;
    }

    public void setMedia_url(List<String> media_url) {
        this.media_url = media_url;
    }

    public List<ProductVariantDTO> getProduct_variant_list() {
        return product_variant_list;
    }

    public void setProduct_variant_list(List<ProductVariantDTO> product_variant_list) {
        this.product_variant_list = product_variant_list;
    }

    public List<FeedbackDTO> getFeedback_list() {
        return feedback_list;
    }

    public void setFeedback_list(List<FeedbackDTO> feedback_list) {
        this.feedback_list = feedback_list;
    }
}
