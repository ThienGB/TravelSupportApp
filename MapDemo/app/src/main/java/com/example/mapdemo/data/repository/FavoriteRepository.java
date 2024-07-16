package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface FavoriteRepository {
    void addOrUpdateFavorite(Favorite favorite);
    void deleteFavorite(String idFavorite);
    void deleteAllFavorite();
    RealmResults<Favorite> getFavoriteList();
    public RealmResults<Accommodation> getFavoriteByIdUser(String idUser);
    Favorite getFavoriteById(String idFavorite);

    List<Accommodation> realmToList(RealmResults<Accommodation> realmAccom);
}
