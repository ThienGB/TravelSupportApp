package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.BookingRepositoryImpl;

import io.realm.RealmList;
import io.realm.RealmResults;

public class UserBookingListViewModel extends ViewModel {
    private BookingRepository bookingRepo;
    private AccommodationRepository accomRepo;
    public UserBookingListViewModel(RealmHelper realmHelper){
        bookingRepo = new BookingRepositoryImpl(realmHelper);
        accomRepo = new AccommodationRepositoryImpl(realmHelper);
    }
    public void deleteBooking(String idBooking) {
        bookingRepo.deleteBooking(idBooking);
    }
    public RealmResults<Booking> getBookingByIdUser(String idUser) {
        return bookingRepo.getBookingByIdUser(idUser);
    }
    public Accommodation getAccomById(String idAccom){
        return accomRepo.getAccomnById(idAccom);
    }
}
