package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;

import java.util.List;

import io.realm.RealmResults;

public interface CityDao {
    void addOrUpdateCity(City city);
    void deleteCity(String idCity);
    void deleteAllCity();
    RealmResults<City> getCityList();
    City getCityById(String idCity);

    List<City> realmResultToList(RealmResults<City> cityRealm);
}
