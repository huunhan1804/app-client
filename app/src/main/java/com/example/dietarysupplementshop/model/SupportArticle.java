package com.example.dietarysupplementshop.model;

public class SupportArticle {
    private Long articleId;
    private SupportCategory category;
    private String articleTitle;
    private String articleContent;
    private Boolean isVisible;
    private Integer viewCount;

    // Getters and Setters
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long id) { this.articleId = id; }
    public SupportCategory getCategory() { return category; }
    public void setCategory(SupportCategory category) { this.category = category; }
    public String getArticleTitle() { return articleTitle; }
    public void setArticleTitle(String title) { this.articleTitle = title; }
    public String getArticleContent() { return articleContent; }
    public void setArticleContent(String content) { this.articleContent = content; }
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean visible) { this.isVisible = visible; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer count) { this.viewCount = count; }
}