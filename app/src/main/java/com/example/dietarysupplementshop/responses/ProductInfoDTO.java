package com.example.dietarysupplementshop.responses;

// Sửa các import để trỏ đến đúng thư mục
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.ApprovalStatus;

// FeedbackDTO và ProductVariantDTO đã nằm trong thư mục responses
import com.example.dietarysupplementshop.responses.FeedbackDTO;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import java.io.Serializable;
import java.util.List;

public class ProductInfoDTO implements Serializable {
    private Long product_id;
    private String product_name;
    private String product_description;
    private double rating;
    private int quantity_in_stock;
    private List<String> media_url;
    private List<ProductVariantDTO> product_variant_list;
    private List<FeedbackDTO> feedback_list;
    private ApprovalStatus approvalStatus;
    private Category category;
    private Integer soldAmount;
    private String product_list_price;
    private String product_sale_price;


    public String getProduct_list_price() { return product_list_price; }
    public void setProduct_list_price(String product_list_price) { this.product_list_price = product_list_price; }
    public String getProduct_sale_price() { return product_sale_price; }
    public void setProduct_sale_price(String product_sale_price) { this.product_sale_price = product_sale_price; }

    public Long getProduct_id() { return product_id; }
    public String getProduct_name() { return product_name; }
    public String getProduct_description() { return product_description; }
    public double getRating() { return rating; }
    public int getQuantity_in_stock() { return quantity_in_stock; }
    public List<String> getMedia_url() { return media_url; }
    public List<ProductVariantDTO> getProduct_variant_list() { return product_variant_list; }
    public List<FeedbackDTO> getFeedback_list() { return feedback_list; }
    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public Category getCategory() { return category; }
    public Integer getSoldAmount() { return soldAmount; }

    public void setProduct_id(Long product_id) { this.product_id = product_id; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }
    public void setProduct_description(String product_description) { this.product_description = product_description; }
    public void setRating(double rating) { this.rating = rating; }
    public void setQuantity_in_stock(int quantity_in_stock) { this.quantity_in_stock = quantity_in_stock; }
    public void setMedia_url(List<String> media_url) { this.media_url = media_url; }
    public void setProduct_variant_list(List<ProductVariantDTO> product_variant_list) { this.product_variant_list = product_variant_list; }
    public void setFeedback_list(List<FeedbackDTO> feedback_list) { this.feedback_list = feedback_list; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }
    public void setCategory(Category category) { this.category = category; }
    public void setSoldAmount(Integer soldAmount) { this.soldAmount = soldAmount; }
}