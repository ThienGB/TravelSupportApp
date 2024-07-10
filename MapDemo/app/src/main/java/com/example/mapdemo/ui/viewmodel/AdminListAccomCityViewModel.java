package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;

import io.realm.RealmResults;

public class AdminListAccomCityViewModel extends ViewModel {
    private AccommodationRepository accomRepo;
    private RealmResults<Accommodation> accoms;
    public AdminListAccomCityViewModel(RealmHelper realmHelper){
        accomRepo = new AccommodationRepositoryImpl(realmHelper);
        loadAccomsAndService();
    }
    private void loadAccomsAndService(){
        accoms = accomRepo.getAccomList();
    }
    public RealmResults<Accommodation> getAccoms(){
        return accoms;
    }
    public RealmResults<Accommodation> getAccomsByCity(String idCity){
        return accomRepo.getAccomsByCity(idCity);
    }
    public void addAccoms(Accommodation accommodation){
        accomRepo.addAccom(accommodation);
    }
    public void updateAccoms(Accommodation accommodation){
        accomRepo.updateAccom(accommodation);
    }
    public void deleteAccoms(String idAccom){
        accomRepo.deleteAccom(idAccom);
    }
}
