package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.BookingDao;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;

public class BookingRepositoryImpl implements BookingRepository {
    private final BookingDao bookingDao;
    private final AccommodationDao accommodationDao;
    @Inject
    public BookingRepositoryImpl(BookingDao bookingDao, AccommodationDao accommodationDao) {
        this.bookingDao = bookingDao;
        this.accommodationDao = accommodationDao;
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
    public RealmList<Accommodation> getBookingAccomByIdUser(String idUser) {
        RealmResults<Booking> listBooking = bookingDao.getBookingByIdUser(idUser);
        RealmList<Accommodation> listAccom = new RealmList<>();
        if (listBooking == null || listBooking.isEmpty()) {
            return listAccom;
        }
        for (Booking booking: listBooking) {
            Accommodation accom = accommodationDao.getAccomnById(booking.getIdTarget());
            listAccom.add(accom);
        }
        return listAccom;
    }

    @Override
    public List<Booking> realmToList(RealmResults<Booking> bookedRealmResult) {
        return bookingDao.realmToList(bookedRealmResult);
    }

    @Override
    public RealmResults<Booking> getBookingByIdUser(String idUser){
        return bookingDao.getBookingByIdUser(idUser);
    }
}
