package com.example.mapdemo.data.remote.firestore;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.helper.CallbackHelper;

public interface FirestoreDataManager {
    void getAccommodationById(String idAccom, CallbackHelper callback);
    void addAccommodation(Accommodation accom);
    void getFavoriteByUserId(String idUser, CallbackHelper callback);
    void getFavoriteAccomById(String idAccom, CallbackHelper callback);
    void addFavorite(Favorite favorite);
    void deleteFavorite(String favoriteId);
    void removeAllListeners();
}
