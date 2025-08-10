package com.example.dietarysupplementshop.requests;

import java.io.Serializable;
import java.util.List;

public class UpdateProductRequest implements Serializable {
    private long product_id;
    private String product_name;
    private String product_description;
    private long category_id;
    private List<UpdateProductVariantRequest> product_variant_list;
    private List<String> image_urls;
    private int quantity_in_stock;
    public UpdateProductRequest() {
    }

    public UpdateProductRequest(long product_id, String product_name, String product_description, long category_id,
                                List<UpdateProductVariantRequest> product_variant_list, List<String> image_urls
                               ) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.product_variant_list = product_variant_list;
        this.image_urls = image_urls;

    }


    public long getProduct_id() { return product_id; }
    public void setProduct_id(long product_id) { this.product_id = product_id; }

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public String getProduct_description() { return product_description; }
    public void setProduct_description(String product_description) { this.product_description = product_description; }

    public long getCategory_id() { return category_id; }
    public void setCategory_id(long category_id) { this.category_id = category_id; }


    public List<UpdateProductVariantRequest> getProduct_variant_list() { return product_variant_list; }

    public void setProduct_variant_list(List<UpdateProductVariantRequest> product_variant_list) { this.product_variant_list = product_variant_list; }

    public List<String> getImage_urls() { return image_urls; }
    public void setImage_urls(List<String> image_urls) { this.image_urls = image_urls; }

    public void setQuantity_in_stock(int quantityInStock) { this.quantity_in_stock = quantityInStock; }
    public int getQuantity_in_stock() { return quantity_in_stock; }


}