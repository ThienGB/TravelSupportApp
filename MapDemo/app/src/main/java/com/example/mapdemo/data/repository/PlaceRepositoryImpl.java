package com.example.mapdemo.data.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mapdemo.data.model.Place;
import com.example.mapdemo.data.remote.ApiService;
import com.example.mapdemo.data.remote.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaceRepositoryImpl implements PlaceRepository{
    private ApiService apiService;

    public PlaceRepositoryImpl(){
        //apiService = RetrofitClient.getApiService(MAP_BASE_URL);
    }
    @Override
    public LiveData<List<Place>> getPlaces(double lat, double lng, int radius, String type, String apiKey) {
        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        String location = lat + "," + lng;
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(apiService.getPlaces(location, radius, type, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeResponse -> {
                    placesLiveData.setValue(placeResponse.getResults());
                }, throwable -> {
                    Log.d("Error", "Error", throwable);
                }));
        return placesLiveData;
    }
}
