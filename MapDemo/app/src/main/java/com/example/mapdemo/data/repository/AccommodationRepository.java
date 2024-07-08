package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.ui.LoadingHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.realm.RealmResults;

public interface AccommodationRepository {
    boolean addAccom(Accommodation accommodation);
    void updateAccom(Accommodation accommodation);
    void deleteAccom(String idAccom);
    RealmResults<Accommodation> getAccomList();
    Accommodation getAccomnById(String idAccom);
    Completable fetchAccommodations(LoadingHelper loadingHelper);
    RealmResults<Accommodation> getAccomsByCity(String idCity);

}
