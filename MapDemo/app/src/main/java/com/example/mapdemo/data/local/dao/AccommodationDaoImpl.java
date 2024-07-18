package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccommodationDaoImpl implements AccommodationDao {
    private final Realm realm;

    @Inject
    public AccommodationDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateAccom(Accommodation accommodation) {
        realm.executeTransactionAsync(r -> r.copyToRealmOrUpdate(accommodation));
    }

    @Override
    public void addOrUpdateListAccom(List<AccommodationResponse> accomRes, CallbackHelper callback) {
        realm.executeTransactionAsync(r -> {
            for (AccommodationResponse acc : accomRes) {
                Accommodation accommodation = new Accommodation(
                        acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                        acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                        acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                        acc.getCityId());
                r.copyToRealmOrUpdate(accommodation);
            }
        }, callback::onComplete);
    }
    public void addOrUpdateAccomCb(Accommodation accommodation, CallbackHelper callback){
        realm.executeTransactionAsync(r -> r.copyToRealmOrUpdate(accommodation), callback::onComplete);
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
        realm.executeTransactionAsync(r -> r.delete(Accommodation.class));
    }

    @Override
    public RealmResults<Accommodation> getAccomList() {
        return realm.where(Accommodation.class).findAll();
    }

    @Override
    public RealmResults<Accommodation> getAccomListByCity(String idCity) {
        return realm.where(Accommodation.class).equalTo("cityId", idCity).findAll();
    }

    @Override
    public RealmResults<Accommodation> getAccomListById(List<String> listIdAccom) {
        return realm.where(Accommodation.class).in("accommodationId", listIdAccom.toArray(new String[0])).findAllAsync();
    }

    @Override
    public Accommodation getAccomnById(String idAccom) {
        return realm.where(Accommodation.class).equalTo("accommodationId", idAccom).findFirst();
    }

    @Override
    public List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm) {
        return realm.copyFromRealm(accomRealm);
    }

}
