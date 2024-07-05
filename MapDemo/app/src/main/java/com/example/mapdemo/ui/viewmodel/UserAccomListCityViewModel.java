package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.ui.LoadingHelper;

import io.realm.RealmResults;

public class UserAccomListCityViewModel extends ViewModel {
    private AccommodationRepository accomRepo;
    private LoadingHelper loadingHelper;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private RealmResults<Accommodation> accoms;
    public UserAccomListCityViewModel(RealmHelper realmHelper){
        accomRepo = new AccommodationRepositoryImpl(realmHelper);
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public RealmResults<Accommodation> getAccoms(){
        accoms = accomRepo.getAccomList();
        return accoms;
    }
    public void fetchAccommodations(){
        isLoading.setValue(true);
        accomRepo.fetchAccommodations(new LoadingHelper() {
            @Override
            public void onLoadingStarted() {
                isLoading.setValue(true);
            }
            @Override
            public void onLoadingFinished() {
                isLoading.setValue(false);
            }
        }).subscribe();
    }
    public RealmResults<Accommodation> getAccomsByCityId(String cityId){
        accoms = accomRepo.getAccomsByCity(cityId);
        return accoms;
    }

    public Accommodation getAccomById(String idAccom){
        return accomRepo.getAccomnById(idAccom);
    }
}
