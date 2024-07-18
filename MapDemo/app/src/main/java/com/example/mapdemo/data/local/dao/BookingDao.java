package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Booking;

import java.util.List;

import io.realm.RealmResults;

public interface BookingDao {
    void addOrUpdateBooking(Booking booking);
    void deleteBooking(String idBooking);
    RealmResults<Booking> getBookingList();
    Booking getBookingById(String idBooking);
    RealmResults<Booking> getBookingByIdUser(String idUser);
    List<Booking> realmToList(RealmResults<Booking> bookedRealmResult);
}
