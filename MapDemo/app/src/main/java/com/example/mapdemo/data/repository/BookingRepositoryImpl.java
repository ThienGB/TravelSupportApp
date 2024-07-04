package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.BookingDao;
import com.example.mapdemo.data.local.dao.BookingDaoImpl;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.ui.LoadingHelper;

import io.realm.RealmResults;

public class BookingRepositoryImpl implements BookingRepository {
    private BookingDao bookingDao;
    public BookingRepositoryImpl(RealmHelper realmHelper) {
        bookingDao = new BookingDaoImpl(realmHelper);
    }
    @Override
    public void addOrUpdateBooking(Booking booking) {
        bookingDao.addOrUpdateBooking(booking);
    }

    @Override
    public void deleteBooking(String idBooking) {
        bookingDao.deleteBooking(idBooking);
    }

    @Override
    public RealmResults<Booking> getBookingList() {
        return bookingDao.getBookingList();
    }

    @Override
    public Booking getBookingById(String idBooking) {
        return bookingDao.getBookingById(idBooking);
    }

    @Override
    public RealmResults<Booking> getBookingByIdUser(String idUser) {
        return bookingDao.getBookingByIdUser(idUser);
    }
}
