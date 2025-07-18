package com.example.dietarysupplementshop.model;

public class HealthArticle {
    private int id;
    private String title;
    private String summary;
    private String category;
    private String author;
    private String publishDate;
    private String imageUrl;
    private String content;

    public HealthArticle(int id, String title, String summary, String category, String author, String publishDate, String imageUrl, String content) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.author = author;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}