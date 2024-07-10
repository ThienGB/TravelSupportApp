package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.BookingRepositoryImpl;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
