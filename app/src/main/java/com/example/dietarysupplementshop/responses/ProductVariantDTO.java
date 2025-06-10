package com.example.dietarysupplementshop.responses;

public class ProductVariantDTO {
    private Long product_variant_id;
    private String product_variant_name;
    private String product_variant_image_url;
    private String origin_price;
    private String sale_price;
    private int quantity_in_stock;

    public ProductVariantDTO(Long product_variant_id, String product_variant_name, String product_variant_image_url, String origin_price, String sale_price, int quantity_in_stock) {
        this.product_variant_id = product_variant_id;
        this.product_variant_name = product_variant_name;
        this.product_variant_image_url = product_variant_image_url;
        this.origin_price = origin_price;
        this.sale_price = sale_price;
        this.quantity_in_stock = quantity_in_stock;
    }

    public ProductVariantDTO() {
    }

    public Long getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(Long product_variant_id) {
        this.product_variant_id = product_variant_id;
    }

    public String getProduct_variant_name() {
        return product_variant_name;
    }

    public void setProduct_variant_name(String product_variant_name) {
        this.product_variant_name = product_variant_name;
    }

    public String getProduct_variant_image_url() {
        return product_variant_image_url;
    }

    public void setProduct_variant_image_url(String product_variant_image_url) {
        this.product_variant_image_url = product_variant_image_url;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }
}
