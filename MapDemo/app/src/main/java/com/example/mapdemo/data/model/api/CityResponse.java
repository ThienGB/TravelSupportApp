package com.example.mapdemo.data.model.api;

import com.google.gson.annotations.SerializedName;

public class CityResponse {
    @SerializedName("city_name")
    private String name;
    @SerializedName("city_image")
    private String image;
    @SerializedName("city_id")
    private String id;

    public CityResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
