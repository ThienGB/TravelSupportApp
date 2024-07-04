package com.example.mapdemo.data.model.api;

import com.google.gson.annotations.SerializedName;

public class AccomServiceRespon {
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
    @SerializedName("id")
    private String id;

    public AccomServiceRespon(String name, int price, int freeroom, String image, String description, String id) {
        this.name = name;
        this.price = price;
        this.freeroom = freeroom;
        this.image = image;
        this.description = description;
        this.id = id;
    }

    public AccomServiceRespon() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
