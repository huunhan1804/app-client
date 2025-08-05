package com.example.dietarysupplementshop.requests;

import java.io.Serializable;
import java.util.List;

public class AddNewProductRequest implements Serializable {

    private String product_name;
    private String product_description;
    private long category_id;
    private int quantity_in_stock;
    private List<String> image_urls;
    private List<AddProductVariantsRequest> product_variant_list;
    private Double product_list_price;
    private Double product_sale_price;

    public AddNewProductRequest(String product_name, String product_description, long category_id,
                                List<String> image_urls,
                                Double list_price, Double sale_price,
                                List<AddProductVariantsRequest> product_variant_list) {
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.image_urls = image_urls;
        this.product_list_price = list_price;
        this.product_sale_price = sale_price;
        this.product_variant_list = product_variant_list;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
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

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }


    public List<AddProductVariantsRequest> getProduct_variant_list() {
        return product_variant_list;
    }

    public void setProduct_variant_list(List<AddProductVariantsRequest> product_variant_list) {
        this.product_variant_list = product_variant_list;
    }

    public Double getProduct_list_price() {
        return product_list_price;
    }

    public void setProduct_list_price(Double product_list_price) {
        this.product_list_price = product_list_price;
    }

    public Double getProduct_sale_price() {
        return product_sale_price;
    }

    public void setProduct_sale_price(Double product_sale_price) {
        this.product_sale_price = product_sale_price;
    }
}