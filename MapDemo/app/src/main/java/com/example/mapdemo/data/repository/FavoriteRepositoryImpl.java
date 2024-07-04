package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.FavoriteDao;
import com.example.mapdemo.data.local.dao.FavoriteDaoImpl;
import com.example.mapdemo.data.model.Favorite;

import io.realm.RealmResults;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private FavoriteDao favoriteDao;

    public FavoriteRepositoryImpl(RealmHelper realmHelper) {
        favoriteDao = new FavoriteDaoImpl(realmHelper);
    }

    @Override
    public void addOrUpdateFavorite(Favorite favorite) {
        favoriteDao.addOrUpdateFavorite(favorite);
    }

    @Override
    public void deleteFavorite(String idFavorite) {
        favoriteDao.deleteFavorite(idFavorite);
    }

    @Override
    public void deleteAllFavorite() {
        favoriteDao.deleteAllFavorite();
    }

    @Override
    public RealmResults<Favorite> getFavoriteList() {
        return favoriteDao.getFavoriteList();
    }

    @Override
    public Favorite getFavoriteById(String idFavorite) {
        return favoriteDao.getFavoriteById(idFavorite);
    }
}
