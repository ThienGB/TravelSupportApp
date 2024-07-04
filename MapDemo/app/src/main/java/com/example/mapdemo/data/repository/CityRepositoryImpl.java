package com.example.mapdemo.data.repository;

import static com.example.mapdemo.data.remote.RetrofitClient.ACCOM_BASE_URL;

import android.annotation.SuppressLint;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.CitiyDaoImpl;
import com.example.mapdemo.data.local.dao.CityDao;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.model.api.CityResponse;
import com.example.mapdemo.data.remote.ApiService;
import com.example.mapdemo.data.remote.RetrofitClient;
import com.example.mapdemo.ui.LoadingHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.realm.RealmResults;

public class CityRepositoryImpl implements CityRepository{
    private CityDao cityDao;
    public CityRepositoryImpl(RealmHelper realmHelper) {
        cityDao = new CitiyDaoImpl(realmHelper);
    }
    @Override
    public boolean addCity(City city) {
        List<City> cities = cityDao.getCityList();
        for (City ct: cities){
            if (city.getIdCity().equals(ct.getIdCity())){
                return false;
            }
        }
        cityDao.addOrUpdateCity(city);
        return true;
    }

    @Override
    public void updateCity(City city) {
        cityDao.addOrUpdateCity(city);
    }

    @Override
    public void deleteCity(String idCity) {
        cityDao.deleteCity(idCity);
    }


    @Override
    public RealmResults<City> getCityList() {
        return cityDao.getCityList();
    }

    @Override
    public City getCityById(String idCity) {
        return cityDao.getCityById(idCity);
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable fetchcities(LoadingHelper loadingHelper) {
        return Completable.create(emitter -> {
            ApiService apiService = RetrofitClient.getApiService(ACCOM_BASE_URL);
            apiService.getCities("city")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (loadingHelper != null) loadingHelper.onLoadingStarted();
                    })
                    .doFinally(() -> {
                        if (loadingHelper != null) loadingHelper.onLoadingFinished();
                    })
                    .subscribe(cityResponses -> saveCitiesToDatabase(cityResponses)
                                    .subscribe(emitter::onComplete, emitter::onError),
                            emitter::onError);
        });
    }
    private Completable saveCitiesToDatabase(List<CityResponse> cityResponseList) {
        return Completable.fromAction(() -> {
            cityDao.deleteAllCity();
            for (CityResponse cityRes : cityResponseList) {
                City city = new City(cityRes.getId(), cityRes.getName(), cityRes.getImage());
                cityDao.addOrUpdateCity(city);
            }
        });
    }
}
