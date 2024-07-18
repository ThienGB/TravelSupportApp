package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.helper.RealmHelper;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookingDaoImpl implements BookingDao {
    private final Realm realm;
    @Inject
    public BookingDaoImpl(RealmHelper realmHelper){
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateBooking(Booking booking) {
        realm.executeTransactionAsync(r -> r.copyToRealmOrUpdate(booking));
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
        return realm.where(Booking.class).findAll();
    }

    @Override
    public Booking getBookingById(String idBooking) {
        return realm.where(Booking.class).equalTo("idBooking", idBooking).findFirst();
    }

    @Override
    public RealmResults<Booking> getBookingByIdUser(String idUser) {
        return realm.where(Booking.class).equalTo("idUser", idUser).findAll();
    }
    @Override
    public List<Booking> realmToList(RealmResults<Booking> bookedRealmResult) {
        return realm.copyFromRealm(bookedRealmResult);
    }
}
