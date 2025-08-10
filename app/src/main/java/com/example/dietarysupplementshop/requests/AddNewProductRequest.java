// File: com/example/dietarysupplementshop/requests/AddNewProductRequest.java
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


    public AddNewProductRequest(String product_name, String product_description, long category_id,
                                int quantity_in_stock, List<String> image_urls, List<AddProductVariantsRequest> product_variant_list) {
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.quantity_in_stock = quantity_in_stock;
        this.image_urls = image_urls;
        this.product_variant_list = product_variant_list;
    }

    // Các getters và setters giữ nguyên
    public String getProduct_name() {
        return product_name;
    }
    public String getProduct_description() {
        return product_description;
    }
    public long getCategory_id() {
        return category_id;
    }
    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }
    public List<String> getImage_urls() {
        return image_urls;
    }
    public List<AddProductVariantsRequest> getProduct_variant_list() {
        return product_variant_list;
    }
}