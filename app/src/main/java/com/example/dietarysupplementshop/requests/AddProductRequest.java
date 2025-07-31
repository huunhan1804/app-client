package com.example.dietarysupplementshop.requests;
import android.net.Uri;

import java.util.List;

public class AddProductRequest {
    private String productName;
    private String description;
    private String category;

    private List<String> imageUrls;
    private List<ProductVariantRequest> variants;

    public AddProductRequest(String productName, String description, String category,
                             List<Uri> imageUrls, List<ProductVariantRequest> variants) {
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.imageUrls = imageUrls;
        this.variants = variants;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<ProductVariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantRequest> variants) {
        this.variants = variants;
    }
}