package com.example.dietarysupplementshop.requests;

public class ProductVariantRequest {
    private String variantName;
    private double originPrice;
    private double salePrice;
    private int quantityInStock;

    public ProductVariantRequest(String variantName, double originPrice, double salePrice, int quantityInStock) {
        this.variantName = variantName;
        this.originPrice = originPrice;
        this.salePrice = salePrice;
        this.quantityInStock = quantityInStock;
    }

    public ProductVariantRequest(String variantName, double price, int quantityInStock) {
        this(variantName, price, price, quantityInStock);
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}