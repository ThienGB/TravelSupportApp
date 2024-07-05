package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.Favorite;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteDaoImpl implements FavoriteDao {
    private Realm realm;
    private RealmHelper realmHelper;
    public FavoriteDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateFavorite(Favorite favorite) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(favorite);
        });
    }

    @Override
    public void deleteFavorite(String idFavorite) {
        realm.executeTransactionAsync(r -> {
            Favorite favorite = r.where(Favorite.class).equalTo("idFavorite", idFavorite).findFirst();
            if (favorite != null) {
                favorite.deleteFromRealm();
            }
        });
    }

    @Override
    public void deleteAllFavorite() {
        realm.executeTransactionAsync(r -> {
            r.delete(Favorite.class);
        });
    }

    @Override
    public RealmResults<Favorite> getFavoriteList() {
        RealmResults<Favorite> realmResults = realm.where(Favorite.class).findAll();
        return realmResults;
    }

    @Override
    public RealmResults<Favorite> getFavoriteByIdUser(String idUser) {
        RealmResults<Favorite> realmResults = realm.where(Favorite.class).equalTo("idUser", idUser).findAll();
        return realmResults;
    }

    @Override
    public Favorite getFavoriteById(String idFavorite) {
        Favorite realmResults = realm.where(Favorite.class).equalTo("idFavorite", idFavorite).findFirst();
        return realmResults;
    }
}
