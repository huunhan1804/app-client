package com.example.dietarysupplementshop.responses;

public class CartItem {
    private long cart_item_id;
    private long product_id;
    private long product_variant_id;
    private int quantity;
    private String sub_total;

    public CartItem() {
    }

    public CartItem(long cart_item_id, long product_id, long product_variant_id, int quantity, String sub_total) {
        this.cart_item_id = cart_item_id;
        this.product_id = product_id;
        this.product_variant_id = product_variant_id;
        this.quantity = quantity;
        this.sub_total = sub_total;
    }

    public long getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(long cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(long product_variant_id) {
        this.product_variant_id = product_variant_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }
}
