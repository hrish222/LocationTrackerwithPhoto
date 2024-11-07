package com.example.LocationOpenStreetApi.entity;

public class StreetInfo {
    private String name;

    public StreetInfo() {
    }

    public StreetInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
