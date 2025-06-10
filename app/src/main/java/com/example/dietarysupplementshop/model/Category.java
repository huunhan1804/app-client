package com.example.dietarysupplementshop.model;

public class Category {
    private Long category_id;
    private String category_name;
    private String category_url;

    public Category(Long category_id, String category_name, String category_url) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_url = category_url;
    }

    public Category() {
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_url() {
        return category_url;
    }

    public void setCategory_url(String category_url) {
        this.category_url = category_url;
    }
}
