package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.helper.CallbackHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AccomInforViewModel extends ViewModel {
    private final AccommodationRepository accomRepo;
    private final FavoriteRepository favoriteRepo;
    private final BookingRepository bookingRepo;
    private final FirebaseBookingRepository firebaseBookingRepo;
    private final FirestoreDataManager firestoreDataManager;
    public FirebaseAuth firebaseAuth;
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    @Inject
    public AccomInforViewModel(AccommodationRepository accomRepo, FavoriteRepository favoriteRepo,
                               BookingRepository bookingRepo, FirebaseBookingRepository firebaseBookingRepo,
                               FirestoreDataManager firestoreDataManager, FirebaseAuth firebaseAuth){
        this.accomRepo = accomRepo;
        this.favoriteRepo = favoriteRepo;
        this.bookingRepo = bookingRepo;
        this.firebaseBookingRepo = firebaseBookingRepo;
        this.firestoreDataManager = firestoreDataManager;
        this.firebaseAuth = firebaseAuth;
    }
    public LiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean isFavorite){
        this.isFavorite.setValue(isFavorite);
    }
    public AccommodationResponse getAccommodationRes(String idAccom){
         Accommodation acc= accomRepo.getAccomnById(idAccom);
         return new AccommodationResponse(
                 acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                 acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                 acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                 acc.getCityId());

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
    public boolean findFavoriteById(String idAccom){
        String idFavorite =firebaseAuth.getCurrentUser().getEmail()+idAccom;
        return favoriteRepo.getFavoriteById(idFavorite) != null;
    }
    public void addBooking(Booking booking){
        bookingRepo.addOrUpdateBooking(booking);
    }
    public Date convertToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
        return calendar.getTime();
    }
    public int getDaysBetween(CalendarDay startDate, CalendarDay endDate) {
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
    public void addFavoriteFirestore(Favorite favorite){
        firestoreDataManager.addFavorite(favorite);
    }
    public void deleteFavoriteFirestore(String favoriteId){
        firestoreDataManager.deleteFavorite(favoriteId);
    }
    public void getAccommodationFirestore(String idAccommodation, CallbackHelper callback){
        firestoreDataManager.getAccommodationById(idAccommodation, new CallbackHelper() {
            @Override
            public void onAccommodationResRecieved(AccommodationResponse acc) {
                Accommodation accommodation = new Accommodation(
                        acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                        acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                        acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                        acc.getCityId());
                accomRepo.addOrUpdateAccomSyn(accommodation, new CallbackHelper() {
                    @Override
                    public void onComplete() {
                        callback.onAccommodationRecieved(accommodation);
                    }
                });

            }
        });
    }
    public void setIsLoading(boolean isLoading){
        this.isLoading.setValue(isLoading);
    }
    public void removeAllListeners() {
        firestoreDataManager.removeAllListeners();
    }
}
