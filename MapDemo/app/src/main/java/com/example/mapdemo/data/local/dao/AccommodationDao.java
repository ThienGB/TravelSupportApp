package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.List;

import io.realm.RealmResults;

public interface AccommodationDao {
    void addOrUpdateAccom(Accommodation accommodation);
    void addOrUpdateAccomCb(Accommodation accommodation, CallbackHelper callback);
    void deleteAccom(String idAccom);
    void deleteAccomByCityId(String idCity);
    void deleteAllAccom();
    RealmResults<Accommodation> getAccomList();
    RealmResults<Accommodation> getAccomListByCity(String idCity);
    RealmResults<Accommodation> getAccomListById(List<String> listIdAccom);
    Accommodation getAccomnById(String idAccom);
    List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm);
    void addOrUpdateListAccom(List<AccommodationResponse> accomRes, CallbackHelper callback);
}
