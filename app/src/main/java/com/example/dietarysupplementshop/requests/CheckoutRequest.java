package com.example.dietarysupplementshop.requests;

import java.util.List;

public class CheckoutRequest {
    List<Long> cart_item_ids;

    public CheckoutRequest(List<Long> cart_item_ids) {
        this.cart_item_ids = cart_item_ids;
    }

    public CheckoutRequest() {
    }

    public List<Long> getCart_item_ids() {
        return cart_item_ids;
    }

    public void setCart_item_ids(List<Long> cart_item_ids) {
        this.cart_item_ids = cart_item_ids;
    }
}
