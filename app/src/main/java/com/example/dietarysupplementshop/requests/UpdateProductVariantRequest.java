package com.example.dietarysupplementshop.requests;

import java.io.Serializable;

public class UpdateProductVariantRequest implements Serializable {
    private Long product_variant_id;
    private String product_variant_name;
    private Double list_price;
    private Double sale_price;
    private int inventory_quantity;
    private int sold_amount;

    public UpdateProductVariantRequest() {}

    public UpdateProductVariantRequest(Long product_variant_id, String product_variant_name, double list_price, double sale_price, int inventory_quantity, int sold_amount) {
        this.product_variant_id = product_variant_id;
        this.product_variant_name = product_variant_name;
        this.list_price = list_price;
        this.sale_price = sale_price;
        this.inventory_quantity = inventory_quantity;
        this.sold_amount = sold_amount;
    }

    public UpdateProductVariantRequest(String product_variant_name, double list_price, double sale_price, int inventory_quantity, int sold_amount) {
        this(null, product_variant_name, list_price, sale_price, inventory_quantity, sold_amount);
    }

    public Long getProduct_variant_id() { return product_variant_id; }
    public void setProduct_variant_id(Long product_variant_id) { this.product_variant_id = product_variant_id; }
    public String getProduct_variant_name() { return product_variant_name; }
    public void setProduct_variant_name(String product_variant_name) { this.product_variant_name = product_variant_name; }
    public double getList_price() { return list_price; }
    public void setList_price(double list_price) { this.list_price = list_price; }
    public double getSale_price() { return sale_price; }
    public void setSale_price(double sale_price) { this.sale_price = sale_price; }
    public int getInventory_quantity() { return inventory_quantity; }
    public void setInventory_quantity(int inventory_quantity) { this.inventory_quantity = inventory_quantity; }
    public int getSold_amount() { return sold_amount; }
    public void setSold_amount(int sold_amount) { this.sold_amount = sold_amount; }
}