package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.CityRepositoryImpl;
import com.example.mapdemo.ui.LoadingHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class UserCityListViewModel extends ViewModel {
    private CityRepository cityRepo;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private RealmResults<City> cities;
    public UserCityListViewModel(RealmHelper realmHelper){
        cityRepo = new CityRepositoryImpl(realmHelper);
        isLoading.setValue(true);
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public void setIsLoading(boolean isLoading){
        this.isLoading.setValue(isLoading);
    }

    public void fetchCities() {
        isLoading.setValue(true);
        cityRepo.fetchcities(new LoadingHelper() {
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
    public RealmResults<City> getCities(){
        cities = cityRepo.getCityList();
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
}
