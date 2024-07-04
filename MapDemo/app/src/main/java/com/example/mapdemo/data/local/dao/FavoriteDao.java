package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.Favorite;

import io.realm.RealmResults;

public interface FavoriteDao {
    void addOrUpdateFavorite(Favorite favorite);
    void deleteFavorite(String idFavorite);
    void deleteAllFavorite();
    RealmResults<Favorite> getFavoriteList();
    Favorite getFavoriteById(String idFavorite);
}
