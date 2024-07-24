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
    public final FirebaseAuth firebaseAuth;
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
    public void loadData(boolean isNetworkAvailable, CallbackHelper callback){
        if (isNetworkAvailable) {
            loadFavoriteAccomFirestore(new CallbackHelper() {
                @Override
                public void onListAccomRecieved(List<Accommodation> accommodations) {
                    loadFavoriteAccomList();
                    callback.onComplete();
                }
                @Override
                public void onListEmpty(){
                    loadFavoriteAccomList();
                    callback.onListEmpty();
                }
            });
        }else {
            loadFavoriteAccomList();
            callback.onNetworkError();
        }
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
    public void handleFavorite(Accommodation accom, boolean isFavorite, boolean isNetworkAvail, CallbackHelper callback){
        if (isNetworkAvail){
            String idFavorite = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()
                    + accom.getAccommodationId();
            if (isFavorite) {
                Favorite favorite = new Favorite(idFavorite,
                        accom.getAccommodationId(),
                        firebaseAuth.getCurrentUser().getEmail(),
                        "accommodation");
                addFavorite(favorite);
                addFavoriteFirestore(favorite);
            } else {
                deleteFavorite(idFavorite);
                deleteFavoriteFirestore(idFavorite);
            }
        }else {
            callback.onNetworkError();
        }
    }
    public List<Accommodation> getFavoriteAccomList(){
        return favoriteAccomList;
    }
    private void loadFavoriteAccomList(){
        this.favoriteAccomList = realmToList(getFavoriteByIdUser(
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()));
        onListChange.postValue(true);
    }
    private void loadFavoriteAccomFirestore(CallbackHelper callback){
        List<Accommodation> favoriteList = new ArrayList<>();
        favoriteRepo.deleteAllFavorite();
        firestoreDataManager.getFavoriteByUserId(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(),
                new CallbackHelper() {
                    @Override
                    public void onListFavoriteRecieved(List<Favorite> favorites) {
                        favoriteRepo.addOrUpdateListFavorite(favorites, new CallbackHelper() {
                            @Override
                            public void onComplete() {
                                for (Favorite favorite: favorites){
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
                    @Override
                    public void onListEmpty(){
                        callback.onListEmpty();
                    }
                });
    }
    private void deleteFavorite(String idFavorite) {
        favoriteRepo.deleteFavorite(idFavorite);
    }
    private void addFavorite(Favorite favorite){
        favoriteRepo.addOrUpdateFavorite(favorite);
    }
    private RealmResults<Accommodation> getFavoriteByIdUser(String idUser) {
        return favoriteRepo.getFavoriteByIdUser(idUser);
    }
    private List<Accommodation> realmToList(RealmResults<Accommodation> realmAccom){
        return favoriteRepo.realmToList(realmAccom);
    }

    private void addFavoriteFirestore(Favorite favorite){
        firestoreDataManager.addFavorite(favorite);
    }
    private void deleteFavoriteFirestore(String favoriteId){
        firestoreDataManager.deleteFavorite(favoriteId);
    }
    @Override
    protected void onCleared(){
        firestoreDataManager.removeAllListeners();
    }

}
