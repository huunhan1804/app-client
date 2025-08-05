package com.example.dietarysupplementshop.responses;


public class ProductVariantDTO {
    private Long product_variant_id;
    private String product_variant_name;
    private String product_variant_image_url;
    private String list_price; // Đổi tên từ origin_price để nhất quán
    private String sale_price;
    private int inventory_quantity; // Đổi tên từ quantity_in_stock để nhất quán

    public ProductVariantDTO(Long product_variant_id, String product_variant_name, String product_variant_image_url, String list_price, String sale_price, int inventory_quantity) {
        this.product_variant_id = product_variant_id;
        this.product_variant_name = product_variant_name;
        this.product_variant_image_url = product_variant_image_url;
        this.sale_price = sale_price;
        this.list_price = list_price;
        this.inventory_quantity = inventory_quantity;

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

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getList_price() {
        return list_price;
    }

    public void setList_price(String list_price) {
        this.list_price = list_price;
    }

    public int getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }
}
