package com.example.dietarysupplementshop.model;

public class SupportCategory {
    private Long supportCategoryId;
    private String supportCategoryName;
    private String supportCategoryDescription;

    public Long getSupportCategoryId() { return supportCategoryId; }
    public void setSupportCategoryId(Long id) { this.supportCategoryId = id; }
    public String getSupportCategoryName() { return supportCategoryName; }
    public void setSupportCategoryName(String name) { this.supportCategoryName = name; }
    public String getSupportCategoryDescription() { return supportCategoryDescription; }
    public void setSupportCategoryDescription(String description) { this.supportCategoryDescription = description; }
}