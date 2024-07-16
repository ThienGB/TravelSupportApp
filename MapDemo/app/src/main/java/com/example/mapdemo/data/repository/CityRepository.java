package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.LoadingHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.realm.RealmResults;

public interface CityRepository {
    boolean addCity(City city);
    void updateCity(City city);
    void deleteCity(String idCity);
    RealmResults<City> getCityList();
    City getCityById(String idCity);
    Completable fetchcities(int countryCode,LoadingHelper loadingHelper, CallbackHelper callback);
    List<City> realmResultToList(RealmResults<City> cityRealm);
}
