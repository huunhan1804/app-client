package com.example.dietarysupplementshop.responses;

public class OrderDetailResponse {
    private ProductInformation productInfoDTO;
    private ProductVariantDTO productVariantDTO;

    private String price;
    private int quantity;
    private String sub_total;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(ProductInformation productInfoDTO, ProductVariantDTO productVariantDTO, String price, int quantity, String sub_total) {
        this.productInfoDTO = productInfoDTO;
        this.productVariantDTO = productVariantDTO;
        this.price = price;
        this.quantity = quantity;
        this.sub_total = sub_total;
    }

    public ProductInformation getProductInfoDTO() {
        return productInfoDTO;
    }

    public void setProductInfoDTO(ProductInformation productInfoDTO) {
        this.productInfoDTO = productInfoDTO;
    }

    public ProductVariantDTO getProductVariantDTO() {
        return productVariantDTO;
    }

    public void setProductVariantDTO(ProductVariantDTO productVariantDTO) {
        this.productVariantDTO = productVariantDTO;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
