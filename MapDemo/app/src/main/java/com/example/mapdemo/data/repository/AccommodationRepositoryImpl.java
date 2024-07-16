package com.example.mapdemo.data.repository;

import static com.example.mapdemo.data.remote.api.RetrofitClient.ACCOM_BASE_URL;

import android.annotation.SuppressLint;

import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.remote.api.ApiService;
import com.example.mapdemo.data.remote.api.RetrofitClient;
import com.example.mapdemo.helper.LoadingHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.realm.RealmResults;

public class AccommodationRepositoryImpl implements AccommodationRepository{
    private AccommodationDao accomDao;
    @Inject
    public AccommodationRepositoryImpl(AccommodationDao accomDao) {
        this.accomDao = accomDao;
    }
    @Override
    public boolean addAccom(Accommodation accommodation) {
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
    public Completable fetchAccommodations(String cityId, LoadingHelper loadingHelper, CallbackHelper callback) {
        return Completable.create(emitter -> {
            ApiService apiService = RetrofitClient.getApiService(ACCOM_BASE_URL);
            apiService.getAccommodations(cityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (loadingHelper != null) loadingHelper.onLoadingStarted();
                    })
                    .flatMapCompletable(accommodationResponses -> {
                        if (accommodationResponses != null && !accommodationResponses.isEmpty()) {
                            return saveAccommodationsToDatabase(accommodationResponses, cityId);
                        } else {
                            return Completable.complete();
                        }
                    })
                    .doFinally(() -> {
                        if (loadingHelper != null) loadingHelper.onLoadingFinished();
                    })
                    .subscribe(emitter::onComplete, throwable -> {
                        accomDao.deleteAccomByCityId(cityId);
                        ErrorResponse error = new ErrorResponse();
                        error.setMessage("This City have no accommodation");
                        callback.onError(error);
                        emitter.onComplete();
                    });
        });
    }
    private Completable saveAccommodationsToDatabase(List<AccommodationResponse> accommodations, String idCity) {
        return Completable.defer(() -> {
            accomDao.deleteAccomByCityId(idCity);
            for (AccommodationResponse acc : accommodations) {
                Accommodation accommodation = new Accommodation(
                        acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                        acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                        acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                        acc.getCityId());
                accomDao.addOrUpdateAccom(accommodation);
            }
            return Completable.complete();
        });
    }

    public RealmResults<Accommodation> getAccomsByCity(String idCity) {
        return accomDao.getAccomListByCity(idCity);
    }

    @Override
    public List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm) {
        List<Accommodation> accomList = accomDao.realmResultToList(accomRealm);
        return accomList;
    }

    @Override
    public void addOrUpdateAccomSyn(Accommodation accommodation, CallbackHelper callback) {
        accomDao.addOrUpdateAccomCb(accommodation, new CallbackHelper() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }
}

