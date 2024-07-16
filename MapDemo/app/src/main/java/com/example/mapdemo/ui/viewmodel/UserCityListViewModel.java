package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.CityRepositoryImpl;
import com.example.mapdemo.helper.LoadingHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserCityListViewModel extends ViewModel {
    private final CityRepository cityRepo;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<ErrorResponse> error = new MutableLiveData<>();
    @Inject
    public UserCityListViewModel(CityRepository cityRepo){
        this.cityRepo = cityRepo;
        isLoading.setValue(false);
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public void setIsLoading(boolean isLoading){
        this.isLoading.setValue(isLoading);
    }

    public LiveData<ErrorResponse> getErrorLiveData() {
        return error;
    }
    public void clearErrorLiveData() {
        error.setValue(null); // Gán giá trị null cho LiveData
    }

    public void fetchCities(int countryCode) {
        isLoading.setValue(true);
        cityRepo.fetchcities(countryCode, new LoadingHelper() {
            @Override
            public void onLoadingStarted() {
                isLoading.setValue(true);
            }
            @Override
            public void onLoadingFinished() {
                isLoading.setValue(false);
            }
        }, new CallbackHelper() {
            @Override
            public void onError(ErrorResponse errorResponse) {
                error.postValue(errorResponse);
            }
        }).subscribe();
    }
    public RealmResults<City> getCities(){
        RealmResults<City> cities = cityRepo.getCityList();
        return cities;
    }

    public List<City> filterCities(List<City> cityList, String newText) {
        List<City> filterCities= new ArrayList<>();
        String query = newText.toLowerCase();
        for (City city: cityList){
            String name = city.getName().toLowerCase();
            if (name.contains(query)) {
                filterCities.add(city);
            }
        }
        return filterCities;
    }
    public List<City> realmResultToList(RealmResults<City> cityRealm){
        List<City> cityList = cityRepo.realmResultToList(cityRealm);
        return cityList;
    }
}
