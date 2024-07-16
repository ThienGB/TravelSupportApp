package com.example.mapdemo.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject {
    @PrimaryKey
    private String idFavorite;
    private String idTarget;
    private String idUser;

    private String type;

    public Favorite(String idFavorite, String idTarget, String idUser, String type) {
        this.idFavorite = idFavorite;
        this.idTarget = idTarget;
        this.idUser = idUser;
        this.type = type;
    }

    public Favorite(String idFavorite, String idTarget) {
        this.idFavorite = idFavorite;
        this.idTarget = idTarget;
    }

    public Favorite() {
    }

    public String getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(String idTarget) {
        this.idTarget = idTarget;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
