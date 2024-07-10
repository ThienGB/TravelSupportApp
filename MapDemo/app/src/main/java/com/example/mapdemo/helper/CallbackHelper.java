package com.example.mapdemo.helper;

import com.example.mapdemo.data.model.api.CityResponse;
import com.example.mapdemo.data.model.api.ErrorResponse;

import java.util.List;

public interface CallbackHelper {
    default void onRoomChecked(boolean isAvailable) {
    }

    default void onDataReceived(int bookedRoom) {

    }
    default void onComplete(){}

    default void onSuccess(List<CityResponse> cities){}
    default void onError(ErrorResponse errorMessage){}
}
