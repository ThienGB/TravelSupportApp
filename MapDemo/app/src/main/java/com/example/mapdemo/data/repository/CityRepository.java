package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.ui.LoadingHelper;

import io.reactivex.rxjava3.core.Completable;
import io.realm.RealmResults;

public interface CityRepository {
    boolean addCity(City city);
    void updateCity(City city);
    void deleteCity(String idCity);
    RealmResults<City> getCityList();
    City getCityById(String idCity);
    Completable fetchcities(LoadingHelper loadingHelper);
}
