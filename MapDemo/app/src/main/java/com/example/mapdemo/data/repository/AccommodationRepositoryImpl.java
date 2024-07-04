package com.example.mapdemo.data.repository;

import static com.example.mapdemo.data.remote.RetrofitClient.ACCOM_BASE_URL;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.model.AccomService;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.remote.ApiService;
import com.example.mapdemo.data.remote.RetrofitClient;
import com.example.mapdemo.ui.LoadingHelper;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.realm.RealmResults;

public class AccommodationRepositoryImpl implements AccommodationRepository{
    private LoadingHelper loadingHelper;
    private AccommodationDao accomDao;
    public AccommodationRepositoryImpl(RealmHelper realmHelper) {
        accomDao = new AccommodationDaoImpl(realmHelper);
    }
    @Override
    public boolean addAccom(Accommodation accommodation) {
        List<Accommodation> accommodations = accomDao.getAccomList();
        for (Accommodation accom: accommodations){
            if (accommodation.getAccommodationId().equals(accom.getAccommodationId())){
                return false;
            }
        }
        accomDao.addOrUpdateAccom(accommodation);
        return true;
    }
    @Override
    public void updateAccom(Accommodation accommodation) {
        accomDao.addOrUpdateAccom(accommodation);
    }
    @Override
    public void deleteAccom(String idAccom) {
        accomDao.deleteAccom(idAccom);
    }

    @Override
    public RealmResults<Accommodation> getAccomList() {
        return accomDao.getAccomList();
    }


    @Override
    public Accommodation getAccomnById(String idAccom) {
        return accomDao.getAccomnById(idAccom);
    }

    @SuppressLint("CheckResult")
    public Completable fetchAccommodations(LoadingHelper loadingHelper) {
        return Completable.create(emitter -> {
            ApiService apiService = RetrofitClient.getApiService(ACCOM_BASE_URL);
            apiService.getAccommodations("accommodation")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (loadingHelper != null) loadingHelper.onLoadingStarted();
                    })
                    .doFinally(() -> {
                        if (loadingHelper != null) loadingHelper.onLoadingFinished();
                    })
                    .subscribe(accommodationResponses -> saveAccommodationsToDatabase(accommodationResponses)
                                    .subscribe(emitter::onComplete, emitter::onError),
                            emitter::onError);
        });
    }
    private Completable saveAccommodationsToDatabase(List<AccommodationResponse> accommodations) {
        return Completable.fromAction(() -> {
            accomDao.deleteAllAccom();
            for (AccommodationResponse acc : accommodations) {
                Accommodation accommodation = new Accommodation(
                        acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                        acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                        acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                        acc.getCityId());
                accomDao.addOrUpdateAccom(accommodation);
            }
        });
    }

    public RealmResults<Accommodation> getAccomsByCity(String idCity) {
        return accomDao.getAccomListByCity(idCity);
    }
}

