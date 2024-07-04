package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.Booking;

import io.realm.RealmResults;

public interface BookingRepository {
    void addOrUpdateBooking(Booking booking);
    void deleteBooking(String idBooking);
    RealmResults<Booking> getBookingList();
    Booking getBookingById(String idBooking);
    RealmResults<Booking> getBookingByIdUser(String idUser);
}
