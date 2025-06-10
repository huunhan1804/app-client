package com.example.dietarysupplementshop.requests;

public class SearchRequest {
    private String keyword;

    public SearchRequest() {
    }

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
