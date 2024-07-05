package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookingDaoImpl implements BookingDao {
    private Realm realm;
    private RealmHelper realmHelper;
    public BookingDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateBooking(Booking booking) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(booking);
        });
    }

    @Override
    public void deleteBooking(String idBooking) {
        realm.executeTransactionAsync(r -> {
            Favorite favorite = r.where(Favorite.class).equalTo("idBooking", idBooking).findFirst();
            if (favorite != null) {
                favorite.deleteFromRealm();
            }
        });
    }

    @Override
    public RealmResults<Booking> getBookingList() {
        RealmResults<Booking> realmResults = realm.where(Booking.class).findAll();
        return realmResults;
    }

    @Override
    public Booking getBookingById(String idBooking) {
        Booking realmResults = realm.where(Booking.class).equalTo("idBooking", idBooking).findFirst();
        return realmResults;
    }

    @Override
    public RealmResults<Booking> getBookingByIdUser(String idUser) {
        RealmResults<Booking> realmResults = realm.where(Booking.class).equalTo("idUser", idUser).findAll();
        return realmResults;
    }
}
