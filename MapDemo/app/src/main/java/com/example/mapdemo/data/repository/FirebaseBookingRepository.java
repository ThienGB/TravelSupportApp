package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.helper.CallbackHelper;

import java.util.Date;

public interface FirebaseBookingRepository {
    void addFirebaseBooking(FirebaseBooking firebaseBooking);

    void getBookedRoomByTime(String idAccom, Date startDate, Date endDate
            , CallbackHelper callback);
}
