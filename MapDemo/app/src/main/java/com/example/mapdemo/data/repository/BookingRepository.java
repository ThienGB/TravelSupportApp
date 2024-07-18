package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface BookingRepository {
    void addOrUpdateBooking(Booking booking);
    void deleteBooking(String idBooking);
    RealmResults<Booking> getBookingList();
    Booking getBookingById(String idBooking);
    RealmResults<Booking> getBookingByIdUser(String idUser);
    RealmList<Accommodation> getBookingAccomByIdUser(String idUser);
    List<Booking> realmToList(RealmResults<Booking> bookedRealmResult);
}
