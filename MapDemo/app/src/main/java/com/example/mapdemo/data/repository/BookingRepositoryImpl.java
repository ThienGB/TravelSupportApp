package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.BookingDao;
import com.example.mapdemo.data.local.dao.BookingDaoImpl;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.ui.LoadingHelper;

import io.realm.RealmList;
import io.realm.RealmResults;

public class BookingRepositoryImpl implements BookingRepository {
    private BookingDao bookingDao;
    private AccommodationDao accommodationDao;
    public BookingRepositoryImpl(RealmHelper realmHelper) {
        bookingDao = new BookingDaoImpl(realmHelper);
        accommodationDao = new AccommodationDaoImpl(realmHelper);
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
    public RealmResults<Booking> getBookingByIdUser(String idUser){
        return bookingDao.getBookingByIdUser(idUser);
    }
}
