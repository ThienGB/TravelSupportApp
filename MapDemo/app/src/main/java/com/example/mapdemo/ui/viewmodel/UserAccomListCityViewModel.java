package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.ui.LoadingHelper;

import java.util.ArrayList;
import java.util.List;

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
    public List<Accommodation> filterAccoms(List<Accommodation> accomList, String newText, int minPrice, int maxPrice) {
        List<Accommodation> filterAccoms= new ArrayList<>();
        String query = newText.toLowerCase();
        for (Accommodation accommodation: accomList){
            String name = accommodation.getName().toLowerCase();
            String address = accommodation.getAddress().toLowerCase();
            if ((name.contains(query) || address.contains(query))
                    && accommodation.getPrice() > minPrice
                    && accommodation.getPrice() < maxPrice){
                filterAccoms.add(accommodation);
            }
        }
        return filterAccoms;
    }
}
