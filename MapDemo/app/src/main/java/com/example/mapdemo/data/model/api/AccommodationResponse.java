package com.example.mapdemo.data.model.api;

import com.google.gson.annotations.SerializedName;

public class AccommodationResponse {
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("freeroom")
    private int freeroom;
    @SerializedName("image")
    private String image;
    @SerializedName("description")
    private String description;
    @SerializedName("address")
    private String address;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("accommodation_id")
    private String accommodationId;

    public AccommodationResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFreeroom() {
        return freeroom;
    }

    public void setFreeroom(int freeroom) {
        this.freeroom = freeroom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        this.accommodationId = accommodationId;
    }
}
