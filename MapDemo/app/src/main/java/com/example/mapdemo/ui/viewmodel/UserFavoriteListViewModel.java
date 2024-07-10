package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FavoriteRepositoryImpl;

import io.realm.RealmList;

public class UserFavoriteListViewModel extends ViewModel {
    private FavoriteRepository favoriteRepo;

    public UserFavoriteListViewModel(RealmHelper realmHelper){
        favoriteRepo = new FavoriteRepositoryImpl(realmHelper);
    }

    public void deleteFavorite(String idFavorite) {
        favoriteRepo.deleteFavorite(idFavorite);
    }

    public RealmList<Accommodation> getFavoriteByIdUser(String idUser) {
        return favoriteRepo.getFavoriteByIdUser(idUser);
    }

}
