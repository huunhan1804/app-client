package com.example.dietarysupplementshop.model;

public class FilterItem {
    private String name;
    private boolean isChecked;

    public FilterItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

