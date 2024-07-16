package com.example.mapdemo.data.local.dao;

import androidx.annotation.NonNull;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.City;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class CitiyDaoImpl implements CityDao{
    private Realm realm;
    @Inject
    public CitiyDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateCity(City city) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(city);
        });
    }
    @Override
    public void deleteCity(String idCity) {
        realm.executeTransactionAsync(r -> {
            City phongBan = r.where(City.class).equalTo("idCity", idCity).findFirst();
            if (phongBan != null) {
                phongBan.deleteFromRealm();
            }
        });
    }
    @Override
    public void deleteAllCity() {
        realm.executeTransactionAsync(r -> {
            r.delete(City.class);
        });
    }
    @Override
    public RealmResults<City> getCityList() {
        RealmResults<City> realmResults = realm.where(City.class).findAllAsync();
        return realmResults;
    }

    @Override
    public City getCityById(String idCity) {
        City realmResults = realm.where(City.class).equalTo("idCity", idCity).findFirst();
        return realmResults;
    }

    @Override
    public List<City> realmResultToList(RealmResults<City> cityRealm) {
        return realm.copyFromRealm(cityRealm);
    }
}
