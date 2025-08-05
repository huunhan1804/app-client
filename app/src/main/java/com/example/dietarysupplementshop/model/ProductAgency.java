package com.example.dietarysupplementshop.model;

import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import java.util.List;

public class ProductAgency {
    private long product_id;
    private long application_id;
    private String product_name;
    private String product_description;
    private List<String> image_url;
    private String category_name;
    private double list_price;
    private double sale_price;
    private int desired_quantity;
    private int inventory_quantity;
    private int sold_amount;
    private String product_status;
    private List<ProductVariantDTO> variants;

    public long getProduct_id() { return product_id; }
    public void setProduct_id(long product_id) { this.product_id = product_id; }

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public List<String> getImage_url() { return image_url; }
    public void setImage_url(List<String> image_url) { this.image_url = image_url; }

    public String getProduct_description() { return product_description; }
    public void setProduct_description(String product_description) { this.product_description = product_description; }

    public String getCategory_name() { return category_name; }
    public void setCategory_name(String category_name) { this.category_name = category_name; }

    public double getList_price() { return list_price; }
    public void setList_price(double list_price) { this.list_price = list_price; }

    public double getSale_price() { return sale_price; }
    public void setSale_price(double sale_price) { this.sale_price = sale_price; }

    public int getDesired_quantity() { return desired_quantity; }
    public void setDesired_quantity(int desired_quantity) { this.desired_quantity = desired_quantity; }

    public int getSold_amount() { return sold_amount; }
    public void setSold_amount(int sold_amount) { this.sold_amount = sold_amount; }

    public String getProduct_status() { return product_status; }
    public void setProduct_status(String product_status) { this.product_status = product_status; }
    public List<ProductVariantDTO> getVariants() { return variants; }
    public void setVariants(List<ProductVariantDTO> variants) { this.variants = variants; }

    public int getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }
}
