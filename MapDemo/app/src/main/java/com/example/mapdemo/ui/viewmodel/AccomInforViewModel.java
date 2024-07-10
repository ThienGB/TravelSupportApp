package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.BookingRepositoryImpl;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FavoriteRepositoryImpl;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepositoryImpl;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AccomInforViewModel extends ViewModel {
    private AccommodationRepository accomRepo;

    private FavoriteRepository favoriteRepo;
    private BookingRepository bookingRepo;
    private MutableLiveData<Boolean> isFavorite = new MutableLiveData<>(false);
    @Inject
    FirebaseBookingRepository firebaseBookingRepo;

    public LiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean isFavorite){
        this.isFavorite.setValue(isFavorite);
    }
    public AccomInforViewModel(RealmHelper realmHelper){
        accomRepo = new AccommodationRepositoryImpl(realmHelper);
        favoriteRepo = new FavoriteRepositoryImpl(realmHelper);
        bookingRepo = new BookingRepositoryImpl(realmHelper);
    }
    public Accommodation getAccommodation(String idAccom){
       return accomRepo.getAccomnById(idAccom);
    }
    public void onFavoriteClicked(){
        if (isFavorite.getValue() != null) {
            isFavorite.setValue(!isFavorite.getValue());
        }
    }
    public void addFavorite(Favorite favorite){
        favoriteRepo.addOrUpdateFavorite(favorite);
    }
    public void deleteFavorite(String idFavorite){
        favoriteRepo.deleteFavorite(idFavorite);
    }
    public boolean findFavoriteById(String idFavorite){
        if (favoriteRepo.getFavoriteById(idFavorite) != null)
            return true;
        return false;
    }
    public void addBooking(Booking booking){
        bookingRepo.addOrUpdateBooking(booking);
    }
    public Date convertToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
        return calendar.getTime();
    }
    public static int getDaysBetween(CalendarDay startDate, CalendarDay endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(startDate.getYear(), startDate.getMonth() - 1, startDate.getDay());
        Calendar endCal = Calendar.getInstance();
        endCal.set(endDate.getYear(), endDate.getMonth() - 1, endDate.getDay());
        long startMillis = startCal.getTimeInMillis();
        long endMillis = endCal.getTimeInMillis();
        long diffMillis = endMillis - startMillis;
        return (int) TimeUnit.MILLISECONDS.toDays(diffMillis);
    }

    public void checkFreeRoom(String accommodationId, Date startDate, Date endDate, int numOfRooms, CallbackHelper callback) {
        Accommodation accommodation = accomRepo.getAccomnById(accommodationId);
        int freeRoom = accommodation.getFreeroom();
        firebaseBookingRepo.getBookedRoomByTime(accommodationId, startDate, endDate, new CallbackHelper() {
            @Override
            public void onDataReceived(int bookedRoom) {
                boolean isAvailable = (freeRoom - bookedRoom) >= numOfRooms;
                callback.onRoomChecked(isAvailable);
            }
        });
    }
    public void addFirebaseBooking(FirebaseBooking firebaseBooking) {
        firebaseBookingRepo.addFirebaseBooking(firebaseBooking);
    }
}
