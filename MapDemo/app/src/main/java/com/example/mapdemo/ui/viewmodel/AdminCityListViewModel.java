package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.CityRepositoryImpl;

import io.realm.RealmResults;

public class AdminCityListViewModel extends ViewModel {
    private CityRepository cityRepo;
    private AccommodationRepository accomRepo;
    private RealmResults<City> cities;
    public AdminCityListViewModel(RealmHelper realmHelper){
        cityRepo = new CityRepositoryImpl(realmHelper);
        accomRepo = new AccommodationRepositoryImpl(realmHelper);
        loadCities();
    }
    private void loadCities(){
        cities = cityRepo.getCityList();
    }
    public RealmResults<City> getCities(){
        return cities;
    }
    public void addCity(City city){
        cityRepo.addCity(city);
    }
    public void UpdateCity(City city){
        cityRepo.updateCity(city);
    }
    public void deleteCity(String idCity){
        cityRepo.deleteCity(idCity);
    }
    public void addAccomToCity(String idAccom, String idCity){
        Accommodation accommodation = accomRepo.getAccomnById(idAccom);
//        Accommodation accom = new Accommodation(accommodation.getAccommodationId(), accommodation.getName(),
//                accommodation.getAddress(), idCity, accommodation.getPrice());
       // accomRepo.updateAccom(accom);
    }
}
