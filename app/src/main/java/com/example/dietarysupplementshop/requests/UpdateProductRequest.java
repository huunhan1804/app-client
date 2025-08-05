package com.example.dietarysupplementshop.requests;

import java.io.Serializable;
import java.util.List;

public class UpdateProductRequest implements Serializable {
    private long product_id;
    private String product_name;
    private String product_description;
    private long category_id;

    // THAY ĐỔI: Sử dụng chung một lớp Request cho biến thể
    private List<AddProductVariantsRequest> product_variant_list;

    private List<String> image_urls;

    // THAY ĐỔI: Bỏ trường status_id vì BE sẽ tự đặt là PENDING
    // private int status_id;

    // THAY ĐỔI: Sử dụng kiểu double để đồng bộ với FE và BE
    private double product_list_price;
    private double product_sale_price;

    // THAY ĐỔI: Thêm constructor không tham số để có thể dùng setter
    public UpdateProductRequest() {
    }

    // Constructor cũ của bạn
    public UpdateProductRequest(long product_id, String product_name, String product_description, long category_id,
                                List<UpdateProductVariantRequest> product_variant_list, List<String> image_urls, int status_id) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        // Lỗi: Kiểu không tương thích
        // this.product_variant_list = product_variant_list;
        this.image_urls = image_urls;
        // this.status_id = status_id;
    }

    // THAY ĐỔI: Thêm constructor mới đầy đủ để tiện sử dụng
    public UpdateProductRequest(long product_id, String product_name, String product_description, long category_id,
                                List<AddProductVariantsRequest> product_variant_list, List<String> image_urls,
                                double product_list_price, double product_sale_price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category_id = category_id;
        this.product_variant_list = product_variant_list;
        this.image_urls = image_urls;
        this.product_list_price = product_list_price;
        this.product_sale_price = product_sale_price;
    }


    public long getProduct_id() { return product_id; }
    public void setProduct_id(long product_id) { this.product_id = product_id; }

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public String getProduct_description() { return product_description; }
    public void setProduct_description(String product_description) { this.product_description = product_description; }

    public long getCategory_id() { return category_id; }
    public void setCategory_id(long category_id) { this.category_id = category_id; }

    // THAY ĐỔI: Getter và Setter cho kiểu dữ liệu mới
    public List<AddProductVariantsRequest> getProduct_variant_list() { return product_variant_list; }
    public void setProduct_variant_list(List<AddProductVariantsRequest> product_variant_list) { this.product_variant_list = product_variant_list; }

    public List<String> getImage_urls() { return image_urls; }
    public void setImage_urls(List<String> image_urls) { this.image_urls = image_urls; }

    // THAY ĐỔI: Bỏ getter/setter của status_id
    // public int getStatus_id() { return status_id; }
    // public void setStatus_id(int status_id) { this.status_id = status_id; }

    public void setProduct_list_price(double productListPrice) { this.product_list_price = productListPrice; }
    public double getProduct_list_price() { return product_list_price; }

    public void setProduct_sale_price(double productSalePrice) { this.product_sale_price = productSalePrice; }
    public double getProduct_sale_price() { return product_sale_price; }
}