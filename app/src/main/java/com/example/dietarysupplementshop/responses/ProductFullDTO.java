package com.example.dietarysupplementshop.responses;

import com.example.dietarysupplementshop.model.Category;

import java.io.Serializable;
import java.util.List;

public class ProductFullDTO implements Serializable {
    private Long product_id;
    private String product_name;
    private String product_description;
    private String product_price;
    private double rating;
    private int quantity_in_stock;
    private List<String> media_url;
    private List<ProductVariantDTO> product_variant_list;
    private List<FeedbackDTO> feedback_list;
    private ApprovalStatusDTO approval_status;
    private Category category;
    private int sold_amount;;

    // Constructors
    public ProductFullDTO() {
    }
    public ProductFullDTO(Long product_id, String product_name, String product_description, double rating, String product_price , int quantity_in_stock, List<String> media_url, List<ProductVariantDTO> product_variant_list, List<FeedbackDTO> feedback_list, ApprovalStatusDTO approval_status, Category category, int sold_amount, String product_list_price, String product_sale_price, boolean isSale) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.rating = rating;
        this.quantity_in_stock = quantity_in_stock;
        this.product_price = product_price;
        this.media_url = media_url;
        this.product_variant_list = product_variant_list;
        this.feedback_list = feedback_list;
        this.approval_status = approval_status;
        this.category = category;
        this.sold_amount = sold_amount;
    }


    // Getters and Setters
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

    public ApprovalStatusDTO getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(ApprovalStatusDTO approval_status) {
        this.approval_status = approval_status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getSold_amount() {
        return sold_amount;
    }

    public void setSold_amount(int sold_amount) {
        this.sold_amount = sold_amount;
    }
    public String getProduct_price() {
        return product_price;
    }
    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }


}