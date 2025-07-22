
package com.example.dietarysupplementshop.requests;

import android.net.Uri;

import java.util.List;

public class AddProductRequest {
    private String productName;
    private String description;
    private String category;
    private String shippingFee;
    private String declarationFileName;
    private String declarationNumber;
    private String declarationDate;
    private String foodSafetyFileName;
    private String otherFilesName;
    private List<Uri> productImageUris;

    private List<ProductVariantRequest> variants;

    public AddProductRequest(String productName, String description, String category,
                             String shippingFee, String declarationFileName,
                             String declarationNumber, String declarationDate, String foodSafetyFileName,
                             String otherFilesName, List<Uri> productImageUris,
                             List<ProductVariantRequest> variants) {
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.shippingFee = shippingFee;
        this.declarationFileName = declarationFileName;
        this.declarationNumber = declarationNumber;
        this.declarationDate = declarationDate;
        this.foodSafetyFileName = foodSafetyFileName;
        this.otherFilesName = otherFilesName;
        this.productImageUris = productImageUris;
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

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getDeclarationFileName() {
        return declarationFileName;
    }

    public void setDeclarationFileName(String declarationFileName) {
        this.declarationFileName = declarationFileName;
    }

    public String getDeclarationNumber() {
        return declarationNumber;
    }

    public void setDeclarationNumber(String declarationNumber) {
        this.declarationNumber = declarationNumber;
    }

    public String getDeclarationDate() {
        return declarationDate;
    }

    public void setDeclarationDate(String declarationDate) {
        this.declarationDate = declarationDate;
    }

    public String getFoodSafetyFileName() {
        return foodSafetyFileName;
    }

    public void setFoodSafetyFileName(String foodSafetyFileName) {
        this.foodSafetyFileName = foodSafetyFileName;
    }

    public String getOtherFilesName() {
        return otherFilesName;
    }

    public void setOtherFilesName(String otherFilesName) {
        this.otherFilesName = otherFilesName;
    }

    public List<Uri> getProductImageUris() {
        return productImageUris;
    }

    public void setProductImageUris(List<Uri> productImageUris) {
        this.productImageUris = productImageUris;
    }

    public List<ProductVariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantRequest> variants) {
        this.variants = variants;
    }
}