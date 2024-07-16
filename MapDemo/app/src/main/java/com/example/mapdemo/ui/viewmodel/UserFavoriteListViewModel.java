package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserFavoriteListViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepo;
    private final AccommodationRepository accommodationRepo;
    private final FirestoreDataManager firestoreDataManager;
    public RealmResults<Accommodation> favoriteAccom;

    @Inject
    public UserFavoriteListViewModel(FavoriteRepository favoriteRepo,
                                     FirestoreDataManager firestoreDataManager,
                                     AccommodationRepository accommodationRepo){
        this.favoriteRepo = favoriteRepo;
        this.firestoreDataManager = firestoreDataManager;
        this.accommodationRepo = accommodationRepo;
    }
    public void deleteFavorite(String idFavorite) {
        favoriteRepo.deleteFavorite(idFavorite);
    }
    public void addFavorite(Favorite favorite){
        favoriteRepo.addOrUpdateFavorite(favorite);
    }
    public RealmResults<Accommodation> getFavoriteByIdUser(String idUser) {
        return favoriteRepo.getFavoriteByIdUser(idUser);
    }
    public void loadFavoriteAccom(String idUser){
        this.favoriteAccom = getFavoriteByIdUser(idUser);
    }
    public RealmResults<Accommodation> getFavoriteAccom(){
        return this.favoriteAccom;
    }
    public List<Accommodation> realmToList(RealmResults<Accommodation> realmAccom){
        return favoriteRepo.realmToList(realmAccom);
    }
    public void loadFavoriteAccomFirestore(String idUser, CallbackHelper callback){
        List<Accommodation> favoriteList = new ArrayList<>();
        firestoreDataManager.getFavoriteByUserId(idUser, new CallbackHelper() {
            @Override
            public void onListFavoriteRecieved(List<Favorite> favorites) {
                for (Favorite favorite: favorites){
                    favoriteRepo.addOrUpdateFavorite(favorite);
                    firestoreDataManager.getAccommodationById(favorite.getIdTarget(), new CallbackHelper() {
                        @Override
                        public void onAccommodationResRecieved(AccommodationResponse acc) {
                            Accommodation accommodation = new Accommodation(
                                    acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                                    acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                                    acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                                    acc.getCityId());
                            accommodationRepo.addAccom(accommodation);
                            favoriteList.add(accommodation);
                            if (favoriteList.size() == favorites.size())
                                callback.onListAccomRecieved(favoriteList);
                        }
                    });
                }

            }
        });
    }
    public void addFavoriteFirestore(Favorite favorite){
        firestoreDataManager.addFavorite(favorite);
    }
    public void deleteFavoriteFirestore(String favoriteId){
        firestoreDataManager.deleteFavorite(favoriteId);
    }
}
