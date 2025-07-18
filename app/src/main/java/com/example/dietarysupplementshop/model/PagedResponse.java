package com.example.dietarysupplementshop.model;

import java.util.List;

public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private boolean last;
    private int number;

    public PagedResponse() {
    }

    public PagedResponse(List<T> content) {
        this.content = content;
        this.totalPages = 1;
        this.last = true;
        this.number = 0;
    }

    public List<T> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public int getNumber() {
        return number;
    }
}