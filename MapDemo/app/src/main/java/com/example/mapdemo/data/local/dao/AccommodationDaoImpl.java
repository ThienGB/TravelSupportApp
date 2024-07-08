package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccommodationDaoImpl implements AccommodationDao {
    private Realm realm;
    private RealmHelper realmHelper;
    public AccommodationDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateAccom(Accommodation accommodation) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(accommodation);
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
        RealmResults<Accommodation> realmResults = realm.where(Accommodation.class).equalTo("cityId", idCity).findAll();
        return realmResults;
    }

    @Override
    public Accommodation getAccomnById(String idAccom) {
        Accommodation realmResults = realm.where(Accommodation.class).equalTo("accommodationId", idAccom).findFirst();
        return realmResults;
    }

}
