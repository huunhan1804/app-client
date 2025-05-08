package com.example.dietarysupplementshop.requests;

import java.io.Serializable;

public class AddToCartRequest implements Serializable {
    private long product_id;
    private long product_variant_id;
    private int quantity;

    public AddToCartRequest() {
    }

    public AddToCartRequest(long product_id, long product_variant_id, int quantity) {
        this.product_id = product_id;
        this.product_variant_id = product_variant_id;
        this.quantity = quantity;
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
}
