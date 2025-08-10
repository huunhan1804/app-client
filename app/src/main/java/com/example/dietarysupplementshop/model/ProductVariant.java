package com.example.dietarysupplementshop.model;

import java.io.Serializable;

public class ProductVariant implements Serializable {
    private Long product_variant_id;
    private Long product_id;
    private String product_variant_name;
    private String origin_price;
    private String sale_price;
    private int inventory_quantity;
    private int sold_amount;
    private int desired_quantity;
    private int quantity_in_stock;
    public ProductVariant() {
    }

    public ProductVariant(Long product_variant_id, Long product_id, String product_variant_name, String origin_price, String sale_price, int inventory_quantity, int sold_amount, int desired_quantity, int quantity_in_stock) {
    this.product_variant_id = product_variant_id;
        this.product_id = product_id;
        this.product_variant_name = product_variant_name;
        this.origin_price  = origin_price;
        this.sale_price = sale_price;
        this.inventory_quantity = inventory_quantity;
        this.sold_amount = sold_amount;
        this.desired_quantity = desired_quantity;
        this.quantity_in_stock = quantity_in_stock;

    }

    // Getters and Setters
    public Long getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(Long product_variant_id) {
        this.product_variant_id = product_variant_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_variant_name() {
        return product_variant_name;
    }

    public void setProduct_variant_name(String product_variant_name) {
        this.product_variant_name = product_variant_name;
    }



    public int getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public int getSold_amount() {
        return sold_amount;
    }

    public void setSold_amount(int sold_amount) {
        this.sold_amount = sold_amount;
    }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "product_variant_id=" + product_variant_id +
                ", product_id=" + product_id +
                ", product_variant_name='" + product_variant_name + '\'' +
                ", origin_price=" + origin_price +
                ", sale_price=" + sale_price +
                ", inventory_quantity=" + inventory_quantity +
                ", sold_amount=" + sold_amount +
                ", desired_quantity=" + desired_quantity +
                ", quantity_in_stock=" + quantity_in_stock+
                '}';
    }
}