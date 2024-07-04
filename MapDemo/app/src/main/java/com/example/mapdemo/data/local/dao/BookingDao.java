package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.City;

import io.realm.RealmResults;

public interface BookingDao {
    void addOrUpdateBooking(Booking booking);
    void deleteBooking(String idBooking);
    RealmResults<Booking> getBookingList();
    Booking getBookingById(String idBooking);
    RealmResults<Booking> getBookingByIdUser(String idUser);
}
