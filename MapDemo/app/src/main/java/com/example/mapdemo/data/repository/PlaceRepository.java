package com.example.mapdemo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.mapdemo.data.model.Place;

import java.util.List;

import io.realm.RealmResults;

public interface PlaceRepository {
    LiveData<List<Place>>  getPlaces(double lat, double lng, int radius, String type, String apiKey);
}
