package com.example.mapdemo.data.repository;

import static android.content.ContentValues.TAG;
import static com.example.mapdemo.data.remote.RetrofitClient.ACCOM_BASE_URL;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.AccomServiceDao;
import com.example.mapdemo.data.local.dao.AccomServiceDaoImpl;
import com.example.mapdemo.data.model.AccomService;
import com.example.mapdemo.data.model.api.AccomServiceRespon;
import com.example.mapdemo.data.remote.ApiService;
import com.example.mapdemo.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccomServiceRepositoryImpl implements AccomServiceRepository{
    private AccomServiceDao accomServiceDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public AccomServiceRepositoryImpl(RealmHelper realmHelper) {
        accomServiceDao = new AccomServiceDaoImpl(realmHelper);
    }
    @Override
    public void fetchServicesFromApi() {
        ApiService apiService = RetrofitClient.getApiService(ACCOM_BASE_URL);
        apiService.getAccomServices("accommodation")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<AccomServiceRespon>>() {
                    @Override
                    public void onNext(@NonNull List<AccomServiceRespon> accommodations) {
                        saveAccommodationsToDatabase(accommodations);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("AccommodationManager", "Error fetching accommodations");
                    }
                    @Override
                    public void onComplete() {
                        Log.e("AccommodationManager", "Error fetching accommodations");
                    }
                });
    }
    private void saveAccommodationsToDatabase(List<AccomServiceRespon> accommodations) {
        for (AccomServiceRespon accommodation : accommodations) {
            AccomService entity = new AccomService();
            entity.setIdAccom(accommodation.getId());
            entity.setPrice(accommodation.getPrice());
            entity.setFreeRoom(accommodation.getFreeroom());
            entity.setImage(accommodation.getImage());
            entity.setDescription(accommodation.getDescription());

            accomServiceDao.addOrUpdateAccomService(entity);
        }
    }
    @Override
    public void addOrUpdateAccomService(AccomService accomService) {
        accomServiceDao.addOrUpdateAccomService(accomService);
    }

    @Override
    public void deleteAccomService(String idAccom) {
        accomServiceDao.deleteAccomService(idAccom);
    }

    @Override
    public AccomService getAccServiceById(String idAccom) {
        return accomServiceDao.getAccomnServiceById(idAccom);
    }

    public void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
