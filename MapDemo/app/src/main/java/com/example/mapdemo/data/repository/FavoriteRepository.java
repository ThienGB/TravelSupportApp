package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Favorite;

import io.realm.RealmResults;

public interface FavoriteRepository {
    void addOrUpdateFavorite(Favorite favorite);
    void deleteFavorite(String idFavorite);
    void deleteAllFavorite();
    RealmResults<Favorite> getFavoriteList();
    Favorite getFavoriteById(String idFavorite);
}
