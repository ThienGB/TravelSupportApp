package com.example.mapdemo.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AccomService extends RealmObject {
    @PrimaryKey
    private String idAccom;
    private int price;
    private int freeRoom;
    private String image;
    private String description;

    public AccomService(String idAccom, int price, int freeRoom, String image, String description) {
        this.idAccom = idAccom;
        this.price = price;
        this.freeRoom = freeRoom;
        this.image = image;
        this.description = description;
    }

    public AccomService() {
    }

    public String getIdAccom() {
        return idAccom;
    }

    public void setIdAccom(String idAccom) {
        this.idAccom = idAccom;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFreeRoom() {
        return freeRoom;
    }

    public void setFreeRoom(int freeRoom) {
        this.freeRoom = freeRoom;
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
}
