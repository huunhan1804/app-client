package com.example.dietarysupplementshop.model;

import java.util.List;

public class District {
    private String name;
    private List<Ward> wards;

    public District() {
    }

    public District(String name, List<Ward> wards) {
        this.name = name;
        this.wards = wards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }
}
