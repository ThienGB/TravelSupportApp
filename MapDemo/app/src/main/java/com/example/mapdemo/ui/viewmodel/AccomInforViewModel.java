package com.example.mapdemo.ui.viewmodel;

import androidx.databinding.ObservableField;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class AccomInforViewModel extends ViewModel {
    private final AccommodationRepository accomRepo;
    private final FavoriteRepository favoriteRepo;
    private final BookingRepository bookingRepo;
    private final FirebaseBookingRepository firebaseBookingRepo;
    private final FirestoreDataManager firestoreDataManager;
    public final FirebaseAuth firebaseAuth;
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> freeRoom = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableField<String> image = new ObservableField<>();
    public final ObservableField<String> address = new ObservableField<>();
    public AccommodationResponse currentAccom;
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
    private void removeAllListeners() {
        firestoreDataManager.removeAllListeners();
    }

    public void onFavoriteClicked(){
        if (isFavorite.getValue() != null) {
            isFavorite.setValue(!isFavorite.getValue());
        }
    }
    public void loadInforAccom(boolean isNetworkAvailable, CallbackHelper callback){
        boolean isFavorite = findFavoriteById(currentAccom.getAccommodationId());
        setFavorite(isFavorite);
        setIsLoading(false);
        if (isNetworkAvailable) {
            setIsLoading(true);
            getAccommodationFirestore(currentAccom.getAccommodationId(), new CallbackHelper() {
                @Override
                public void onAccommodationRecieved(Accommodation accommodation) {
                    loadLocalData();
                    setIsLoading(false);
                }
            });
        } else {
            loadLocalData();
            callback.onNetworkError();
        }
    }
    public void handleFavorite(Boolean isFavorite, CallbackHelper callback){
        String idFavorite = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()
                + currentAccom.getAccommodationId();
        if (isFavorite) {
            Favorite favorite = new Favorite(idFavorite,
                    currentAccom.getAccommodationId(),
                    firebaseAuth.getCurrentUser().getEmail(),
                    "accommodation");
            addFavorite(favorite);
            addFavoriteFirestore(favorite);
            callback.onFavorite("Add "+ currentAccom.getName()+ " to favorite list successfully.");
        } else {
            deleteFavorite(idFavorite);
            deleteFavoriteFirestore(idFavorite);
            callback.onFavorite("Remove "+ currentAccom.getName()+ " from favorite list successfully.");
        }
    }
    public void handleBooking(List<CalendarDay> selectedDates, int numOfRooms, CallbackHelper callback){
        Date startDate = convertToDate(selectedDates.get(0));
        Date endDate = convertToDate(selectedDates.get(selectedDates.size() - 1));
        checkFreeRoom(currentAccom.getAccommodationId(), startDate, endDate, numOfRooms, new CallbackHelper() {
            @Override
            public void onRoomChecked(boolean isAvailable) {
                if (isAvailable) {
                    String idBooking = UUID.randomUUID().toString();
                    int price = currentAccom.getPrice() * getDaysBetween(selectedDates.get(0),
                            selectedDates.get(selectedDates.size() - 1)) * numOfRooms;
                    String idUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                    FirebaseBooking firebaseBooking = new FirebaseBooking(idBooking,
                            currentAccom.getAccommodationId(), idUser, startDate.getTime(),
                            endDate.getTime(), price, numOfRooms);
                    Booking booking = new Booking(idBooking, currentAccom.getAccommodationId(),
                            idUser, startDate, endDate, price, numOfRooms);
                    addBooking(booking);
                    addFirebaseBooking(firebaseBooking);
                    callback.onComplete();
                } else {
                    callback.onOutOfRoom();
                }
            }
        });
    }
    private void addFavorite(Favorite favorite){
        favoriteRepo.addOrUpdateFavorite(favorite);
    }
    private void deleteFavorite(String idFavorite){
        favoriteRepo.deleteFavorite(idFavorite);
    }
    private boolean findFavoriteById(String idAccom){
        String idFavorite = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()+idAccom;
        return favoriteRepo.getFavoriteById(idFavorite) != null;
    }
    private void addBooking(Booking booking){
        bookingRepo.addOrUpdateBooking(booking);
    }
    private Date convertToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
        return calendar.getTime();
    }
    private int getDaysBetween(CalendarDay startDate, CalendarDay endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(startDate.getYear(), startDate.getMonth() - 1, startDate.getDay());
        Calendar endCal = Calendar.getInstance();
        endCal.set(endDate.getYear(), endDate.getMonth() - 1, endDate.getDay());
        long startMillis = startCal.getTimeInMillis();
        long endMillis = endCal.getTimeInMillis();
        long diffMillis = endMillis - startMillis;
        return (int) TimeUnit.MILLISECONDS.toDays(diffMillis);
    }

    private void checkFreeRoom(String accommodationId, Date startDate, Date endDate, int numOfRooms, CallbackHelper callback) {
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
    private void addFirebaseBooking(FirebaseBooking firebaseBooking) {
        firebaseBookingRepo.addFirebaseBooking(firebaseBooking);
    }
    private void addFavoriteFirestore(Favorite favorite){
        firestoreDataManager.addFavorite(favorite);
    }
    private void deleteFavoriteFirestore(String favoriteId){
        firestoreDataManager.deleteFavorite(favoriteId);
    }
    private void getAccommodationFirestore(String idAccommodation, CallbackHelper callback){
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
    private void setIsLoading(boolean isLoading){
        this.isLoading.setValue(isLoading);
    }
    private void setFavorite(boolean isFavorite){
        this.isFavorite.setValue(isFavorite);
    }
    private AccommodationResponse getAccommodationRes(String idAccom){
        Accommodation acc= accomRepo.getAccomnById(idAccom);
        return new AccommodationResponse(
                acc.getAccommodationId(), acc.getName(), acc.getPrice(),
                acc.getFreeroom(), acc.getImage(), acc.getDescription(),
                acc.getAddress(), acc.getLongitude(), acc.getLatitude(),
                acc.getCityId());

    }
    private void loadLocalData(){
        currentAccom = getAccommodationRes(currentAccom.getAccommodationId());
        name.set(currentAccom.getName());
        freeRoom.set(String.valueOf(currentAccom.getFreeroom()));
        description.set(currentAccom.getDescription());
        price.set(String.valueOf(currentAccom.getPrice()));
        address.set(currentAccom.getAddress());
        image.set(currentAccom.getImage());
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        removeAllListeners();
    }
}
