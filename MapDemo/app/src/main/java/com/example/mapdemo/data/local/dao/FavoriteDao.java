package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.List;

import io.realm.RealmResults;

public interface FavoriteDao {
    void addOrUpdateFavorite(Favorite favorite);
    void deleteFavorite(String idFavorite);
    void deleteAllFavorite();
    RealmResults<Favorite> getFavoriteList();
    RealmResults<Favorite> getFavoriteByIdUser(String idUser);
    Favorite getFavoriteById(String idFavorite);
    void addOrUpdateListFavorite(List<Favorite> favorites, CallbackHelper callback);
}
