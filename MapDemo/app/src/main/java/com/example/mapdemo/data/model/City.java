package com.example.mapdemo.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {
    @PrimaryKey
    private String idCity;
    private String name;
    private String image;

    public City(String idCity, String name, String image) {
        this.idCity = idCity;
        this.name = name;
        this.image = image;
    }

    public City(String idCity, String name) {
        this.idCity = idCity;
        this.name = name;
    }

    public City() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
