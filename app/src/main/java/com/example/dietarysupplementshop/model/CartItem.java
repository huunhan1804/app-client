package com.example.dietarysupplementshop.model;

import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;

public class CartItem {

    private Long cart_item_id;
    private ProductInformation product_info;
    private ProductVariantDTO product_variant_info;
    private int quantity;
    private String subTotal;

    private boolean isSelected;

    public CartItem() {
    }

    public CartItem(Long cart_item_id, ProductInformation product_info, ProductVariantDTO product_variant_info, int quantity, String subTotal) {
        this.cart_item_id = cart_item_id;
        this.product_info = product_info;
        this.product_variant_info = product_variant_info;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public Long getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(Long cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public ProductInformation getProduct_info() {
        return product_info;
    }

    public void setProduct_info(ProductInformation product_info) {
        this.product_info = product_info;
    }

    public ProductVariantDTO getProduct_variant_info() {
        return product_variant_info;
    }

    public void setProduct_variant_info(ProductVariantDTO product_variant_info) {
        this.product_variant_info = product_variant_info;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubToal(String subTotal) {
        this.subTotal = subTotal;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
