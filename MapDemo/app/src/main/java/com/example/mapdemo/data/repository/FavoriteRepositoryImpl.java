package com.example.mapdemo.data.repository;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.FavoriteDao;
import com.example.mapdemo.data.local.dao.FavoriteDaoImpl;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;

import io.realm.RealmList;
import io.realm.RealmResults;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private FavoriteDao favoriteDao;
    private AccommodationDao accommodationDao;

    public FavoriteRepositoryImpl(RealmHelper realmHelper) {
        favoriteDao = new FavoriteDaoImpl(realmHelper);
        accommodationDao = new AccommodationDaoImpl(realmHelper);
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
    public RealmList<Accommodation> getFavoriteByIdUser(String idUser) {
        RealmResults<Favorite> listFavorite = favoriteDao.getFavoriteByIdUser(idUser);
        RealmList<Accommodation> listAccom = new RealmList<>();
        if (listFavorite == null || listFavorite.isEmpty()) {
            return listAccom;
        }
        for (Favorite favorite: listFavorite) {
            Accommodation accom = accommodationDao.getAccomnById(favorite.getIdTarget());
            listAccom.add(accom);
        }
        return listAccom;
    }

    @Override
    public Favorite getFavoriteById(String idFavorite) {
        return favoriteDao.getFavoriteById(idFavorite);
    }
}
