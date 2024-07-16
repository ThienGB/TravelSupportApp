package com.example.mapdemo.data.repository;

import static com.example.mapdemo.data.remote.api.RetrofitClient.CITY_BASE_URL;

import android.annotation.SuppressLint;

import com.example.mapdemo.data.local.dao.CityDao;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.CityResponse;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.remote.api.ApiService;
import com.example.mapdemo.data.remote.api.RetrofitClient;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.LoadingHelper;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.realm.RealmResults;
import okhttp3.ResponseBody;

public class CityRepositoryImpl implements CityRepository{
    private CityDao cityDao;
    @Inject
    public CityRepositoryImpl(CityDao cityDao) {
        this.cityDao = cityDao;
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
    public Completable fetchcities(int countryCode,LoadingHelper loadingHelper, CallbackHelper callback) {
        ApiService apiService = RetrofitClient.getApiService(CITY_BASE_URL);
        return Completable.create(emitter -> {
            apiService.getCities(countryCode)
                    .subscribeOn(Schedulers.io())
                    .flatMapCompletable(response -> {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String jsonString = responseBody.string();
                                Gson gson = new Gson();
                                JsonElement jsonElement = JsonParser.parseString(jsonString);
                                if (jsonElement.isJsonArray()) {
                                    Type listType = new TypeToken<List<CityResponse>>() {}.getType();
                                    List<CityResponse> cities = gson.fromJson(jsonElement, listType);
                                    return saveCitiesToDatabase(cities);
                                } else {
                                    ErrorResponse errorResponse = gson.fromJson(jsonElement, ErrorResponse.class);
                                    callback.onError(errorResponse);
                                    return Completable.complete();
                                }
                            }else {
                                callback.onError(new ErrorResponse(500, "Empty response body"));
                                return Completable.complete();
                            }
                        } else {
                            callback.onError(new ErrorResponse(response.code(), "Request failed"));
                            return Completable.complete();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (loadingHelper != null) loadingHelper.onLoadingStarted();
                    })
                    .doFinally(() -> {
                        if (loadingHelper != null) loadingHelper.onLoadingFinished();
                    })
                    .subscribe(emitter::onComplete, emitter::onError);
        });
    }

    @Override
    public List<City> realmResultToList(RealmResults<City> cityRealm) {
        return cityDao.realmResultToList(cityRealm);
    }

    public Completable saveCitiesToDatabase(List<CityResponse> cityResponseList) {
        return Completable.defer(() -> {
            cityDao.deleteAllCity();
            for (CityResponse cityRes : cityResponseList) {
                City city = new City(cityRes.getId(), cityRes.getName(), cityRes.getImage());
                cityDao.addOrUpdateCity(city);
            }
            return Completable.complete();
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
