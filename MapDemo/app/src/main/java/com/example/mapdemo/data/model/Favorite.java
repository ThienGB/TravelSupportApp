package com.example.mapdemo.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject {
    @PrimaryKey
    private String idFavorite;
    private String type;

    public Favorite(String idFavorite, String type) {
        this.idFavorite = idFavorite;
        this.type = type;
    }

    public Favorite() {
    }

    public String getIdFavorite() {
        return idFavorite;
    }

    public void setIdFavorite(String idFavorite) {
        this.idFavorite = idFavorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
