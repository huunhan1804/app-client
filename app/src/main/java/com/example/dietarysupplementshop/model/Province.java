package com.example.dietarysupplementshop.model;

import java.util.List;

public class Province {
    private String name;
    private List<District> districts;

    public Province() {
    }

    public Province(String name, List<District> districts) {
        this.name = name;
        this.districts = districts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
