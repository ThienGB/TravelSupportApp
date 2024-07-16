package com.example.mapdemo.data.repository;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.FavoriteDao;
import com.example.mapdemo.data.local.dao.FavoriteDaoImpl;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private FavoriteDao favoriteDao;
    private AccommodationDao accommodationDao;
    @Inject
    public FavoriteRepositoryImpl(FavoriteDao favoriteDao, AccommodationDao accommodationDao) {
        this.accommodationDao = accommodationDao;
        this.favoriteDao = favoriteDao;
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
    public RealmResults<Accommodation> getFavoriteByIdUser(String idUser) {
        RealmResults<Favorite> listFavorite = favoriteDao.getFavoriteByIdUser(idUser);
        if (listFavorite == null || listFavorite.isEmpty()) {
            return accommodationDao.getAccomListByCity("");
        }
        List<String> idTargets = new ArrayList<>();
        for (Favorite favorite : listFavorite) {
            idTargets.add(favorite.getIdTarget());
        }
        return accommodationDao.getAccomListById(idTargets);
    }

    @Override
    public Favorite getFavoriteById(String idFavorite) {
        return favoriteDao.getFavoriteById(idFavorite);
    }

    @Override
    public List<Accommodation> realmToList(RealmResults<Accommodation> realmAccom) {
        return accommodationDao.realmResultToList(realmAccom);
    }
}
