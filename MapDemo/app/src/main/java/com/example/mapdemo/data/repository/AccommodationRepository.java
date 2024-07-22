package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.realm.RealmResults;

public interface AccommodationRepository {
    void addAccom(Accommodation accommodation);
    void updateAccom(Accommodation accommodation);
    void deleteAccom(String idAccom);
    RealmResults<Accommodation> getAccomList();
    Accommodation getAccomnById(String idAccom);
    Completable fetchAccommodations(String cityId, CallbackHelper callback);
    RealmResults<Accommodation> getAccomsByCity(String idCity);
    List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm);
    void addOrUpdateAccomSyn(Accommodation accommodation, CallbackHelper callback);
}
