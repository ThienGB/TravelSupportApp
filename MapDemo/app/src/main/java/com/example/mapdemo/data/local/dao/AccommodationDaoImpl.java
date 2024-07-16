package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccommodationDaoImpl implements AccommodationDao {
    private Realm realm;

    @Inject
    public AccommodationDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateAccom(Accommodation accommodation) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(accommodation);
        });
    }
    public void addOrUpdateAccomCb(Accommodation accommodation, CallbackHelper callback){
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(accommodation);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onComplete();
            }
        });
    }
    @Override
    public void deleteAccom(String idAccom) {
        realm.executeTransactionAsync(r -> {
            Accommodation accommodation = r.where(Accommodation.class).equalTo("accommodationId", idAccom).findFirst();
            if (accommodation != null) {
                accommodation.deleteFromRealm();
            }
        });
    }

    @Override
    public void deleteAccomByCityId(String idCity) {
        realm.executeTransactionAsync(realm -> {
            RealmResults<Accommodation> results = realm.where(Accommodation.class)
                    .equalTo("cityId", idCity)
                    .findAll();
            results.deleteAllFromRealm();
        });
    }

    public void deleteAllAccom() {
        realm.executeTransactionAsync(r -> {
            r.delete(Accommodation.class);
        });
    }

    @Override
    public RealmResults<Accommodation> getAccomList() {
        RealmResults<Accommodation> realmResults = realm.where(Accommodation.class).findAll();
        return realmResults;
    }

    @Override
    public RealmResults<Accommodation> getAccomListByCity(String idCity) {
        RealmResults<Accommodation> realmResults = realm.where(Accommodation.class).equalTo("cityId", idCity).findAllAsync();
        return realmResults;
    }

    @Override
    public RealmResults<Accommodation> getAccomListById(List<String> listIdAccom) {
        return realm.where(Accommodation.class).in("accommodationId", listIdAccom.toArray(new String[0])).findAllAsync();
    }

    @Override
    public Accommodation getAccomnById(String idAccom) {
        Accommodation realmResults = realm.where(Accommodation.class).equalTo("accommodationId", idAccom).findFirst();
        return realmResults;
    }

    @Override
    public List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm) {
        return realm.copyFromRealm(accomRealm);
    }

}
