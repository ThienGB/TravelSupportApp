package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;

import io.realm.RealmResults;

public interface AccommodationDao {
    void addOrUpdateAccom(Accommodation accommodation);
    void deleteAccom(String idAccom);
    void deleteAccomByCityId(String idCity);
    void deleteAllAccom();
    RealmResults<Accommodation> getAccomList();
    RealmResults<Accommodation> getAccomListByCity(String idCity);
    Accommodation getAccomnById(String idAccom);
}
