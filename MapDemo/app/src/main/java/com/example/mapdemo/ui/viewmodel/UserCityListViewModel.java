package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.realm.RealmResults;

public class UserCityListViewModel extends ViewModel {
    private final CityRepository cityRepo;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();
    public List<City> cityListFilter;
    private List<City> cityListOrigin;
    private RealmResults<City> cityRealmResults;
    @Inject
    public UserCityListViewModel(CityRepository cityRepo){
        this.cityRepo = cityRepo;
        cityListFilter = new ArrayList<>();
        cityListOrigin = new ArrayList<>();
    }
    public void fetchData(boolean isNetworkAvailable, int countryCode, CallbackHelper callback){
        clearErrorLiveData();
        if (isNetworkAvailable) {
            fetchCities(countryCode);
        } else {
            loadCityList();
            callback.onNetworkError();
        }
    }
    public List<City> getCitiList(){
        return cityListFilter;
    }
    public void filterCities(String newText) {
        cityListFilter= new ArrayList<>();
        String query = newText.toLowerCase();
        for (City city: cityListOrigin){
            String name = city.getName().toLowerCase();
            if (name.contains(query)) {
                cityListFilter.add(city);
            }
        }
        onListChange.postValue(true);
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorLiveData() {
        return error;
    }
    public void clearErrorLiveData() {
        error.setValue(null);
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
    private List<City> realmToList(RealmResults<City> cityRealm){
        return cityRepo.realmResultToList(cityRealm);
    }
    private void fetchCities(int countryCode) {
        cityRepo.fetchcities(countryCode, new CallbackHelper() {
            @Override
            public void onStart() {
                if (countryCode != 2)
                    loadCityList();
                if (getCitiList().size() == 0){
                    isLoading.setValue(true);
                }
            }
            @Override
            public void onComplete() {
                isLoading.setValue(false);
                if (countryCode != 2)
                    loadCityList();
            }
            @Override
            public void onError(ErrorResponse errorResponse) {
                error.postValue(errorResponse.getMessage());
            }
        }).subscribe();
    }
    private void loadCityList(){
        cityRealmResults = cityRepo.getCityList();
        cityListOrigin = realmToList(cityRealmResults);
        cityListFilter = cityListOrigin;
        onListChange.postValue(true);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (cityRealmResults != null) {
            cityRealmResults.removeAllChangeListeners();
        }
    }
}
