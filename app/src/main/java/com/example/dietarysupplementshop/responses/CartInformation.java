package com.example.dietarysupplementshop.responses;

import com.example.dietarysupplementshop.model.CartItem;

import java.util.List;

public class CartInformation {
    private long id;
    private int total_item;
    private List<CartItem> cart_item;

    public CartInformation() {
    }

    public CartInformation(long id, int total_item, List<CartItem> cart_item) {
        this.id = id;
        this.total_item = total_item;
        this.cart_item = cart_item;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotal_item() {
        return total_item;
    }

    public void setTotal_item(int total_item) {
        this.total_item = total_item;
    }

    public List<CartItem> getCartItem() {
        return cart_item;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cart_item = cartItem;
    }
}
