package com.example.mapdemo.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Accommodation extends RealmObject {
    @PrimaryKey
    private String accommodationId;
    private String name;
    private int price;
    private int freeroom;
    private int currentFreeroom;
    private String image;
    private String description;
    private String address;
    private String longitude;
    private String latitude;
    private String cityId;

    public Accommodation(String accommodationId, String name, int price, int freeroom, String image, String description, String address, String longitude, String latitude, String cityId) {
        this.accommodationId = accommodationId;
        this.name = name;
        this.price = price;
        this.freeroom = freeroom;
        this.image = image;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cityId = cityId;
    }

    public Accommodation(String accommodationId, String name, int price, String cityId) {
        this.accommodationId = accommodationId;
        this.name = name;
        this.price = price;
        this.cityId = cityId;
    }

    public Accommodation(String accommodationId, String name, int price) {
        this.accommodationId = accommodationId;
        this.name = name;
        this.price = price;
    }

    public Accommodation() {
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        this.accommodationId = accommodationId;
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

    public int getCurrentFreeroom() {
        return currentFreeroom;
    }

    public void setCurrentFreeroom(int currentFreeroom) {
        this.currentFreeroom = currentFreeroom;
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
}
