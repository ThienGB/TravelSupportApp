package com.example.mapdemo.helper;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.model.api.ErrorResponse;

import java.util.List;

public interface CallbackHelper {
    default void onRoomChecked(boolean isAvailable) {}
    default void onDataReceived(int bookedRoom) {}
    default void onComplete(){}
    default void onError(ErrorResponse errorMessage){}
    default void onListFavoriteRecieved(List<Favorite> favorites){}
    default void onAccommodationResRecieved(AccommodationResponse accommodation){}
    default void onAccommodationRecieved(Accommodation accommodation){}
    default void onListAccomRecieved(List<Accommodation> accommodations){}
}
