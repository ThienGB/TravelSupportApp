package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.helper.CallbackHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserFavoriteListViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepo;
    private final AccommodationRepository accommodationRepo;
    private final FirestoreDataManager firestoreDataManager;
    public FirebaseAuth firebaseAuth;
    public List<Accommodation> favoriteAccomList;
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();

    @Inject
    public UserFavoriteListViewModel(FavoriteRepository favoriteRepo,
                                     FirestoreDataManager firestoreDataManager,
                                     AccommodationRepository accommodationRepo,
                                     FirebaseAuth firebaseAuth){
        this.favoriteRepo = favoriteRepo;
        this.firestoreDataManager = firestoreDataManager;
        this.accommodationRepo = accommodationRepo;
        this.firebaseAuth = firebaseAuth;
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
    public void loadFavoriteAccomList(){
        this.favoriteAccomList = realmToList(getFavoriteByIdUser(
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()));
        onListChange.postValue(true);
    }
    public List<Accommodation> getFavoriteAccomList(){
        return favoriteAccomList;
    }
    public List<Accommodation> realmToList(RealmResults<Accommodation> realmAccom){
        return favoriteRepo.realmToList(realmAccom);
    }
    public void loadFavoriteAccomFirestore(CallbackHelper callback){
        List<Accommodation> favoriteList = new ArrayList<>();
        firestoreDataManager.getFavoriteByUserId(
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(),
                new CallbackHelper() {
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
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
}
