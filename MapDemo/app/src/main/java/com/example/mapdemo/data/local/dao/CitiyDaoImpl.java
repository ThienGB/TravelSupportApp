package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.CityResponse;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class CitiyDaoImpl implements CityDao{
    private final Realm realm;
    @Inject
    public CitiyDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateCity(City city) {
        realm.executeTransactionAsync(r -> r.copyToRealmOrUpdate(city));
    }
    @Override
    public void addOrUpdateListCity(List<CityResponse> cityRespon, CallbackHelper callback) {
        realm.executeTransactionAsync(r -> {
            for (CityResponse cityRes : cityRespon) {
                City city = new City(cityRes.getId(), cityRes.getName(), cityRes.getImage());
                r.copyToRealmOrUpdate(city);
            }
        }, callback::onComplete);
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
        realm.executeTransactionAsync(r -> r.delete(City.class));
    }
    @Override
    public RealmResults<City> getCityList() {
        return realm.where(City.class).findAllAsync();
    }

    @Override
    public City getCityById(String idCity) {
        return realm.where(City.class).equalTo("idCity", idCity).findFirst();
    }

    @Override
    public List<City> realmResultToList(RealmResults<City> cityRealm) {
        return realm.copyFromRealm(cityRealm);
    }
}
