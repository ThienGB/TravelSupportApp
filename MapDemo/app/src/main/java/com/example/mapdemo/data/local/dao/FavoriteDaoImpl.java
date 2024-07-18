package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Favorite;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteDaoImpl implements FavoriteDao {
    private final Realm realm;
    @Inject
    public FavoriteDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateFavorite(Favorite favorite) {
        realm.executeTransactionAsync(r -> r.copyToRealmOrUpdate(favorite));
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
        realm.executeTransactionAsync(r -> r.delete(Favorite.class));
    }

    @Override
    public RealmResults<Favorite> getFavoriteList() {
        return realm.where(Favorite.class).findAll();
    }

    @Override
    public RealmResults<Favorite> getFavoriteByIdUser(String idUser) {
        return realm.where(Favorite.class).equalTo("idUser", idUser).findAll();
    }

    @Override
    public Favorite getFavoriteById(String idFavorite) {
        return realm.where(Favorite.class).equalTo("idFavorite", idFavorite).findFirst();
    }
}
