package com.example.mapdemo.ui.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.helper.CallbackHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import io.realm.RealmResults;

public class UserAccomListCityViewModel extends ViewModel {
    private final AccommodationRepository accomRepo;
    private final FirebaseBookingRepository firebaseBookingRepo;
    private final FirestoreDataManager firestoreDataManager;
    private List<Accommodation> accomListOrigin;
    private List<Accommodation> accomListFilter;
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();
    private City currentCity;
    public final ObservableField<String> price = new ObservableField<>();

    @Inject
    public UserAccomListCityViewModel(AccommodationRepository accomRepo,
                                      FirebaseBookingRepository firebaseBookingRepo,
                                      FirestoreDataManager firestoreDataManager){
        this.accomRepo = accomRepo;
        this.firebaseBookingRepo = firebaseBookingRepo;
        this.firestoreDataManager = firestoreDataManager;
        setPrice("VND 0 - VND 10.000.000");
    }
    public void fetchData(boolean isNetworkAvailable, CallbackHelper callback){
        clearErrorLiveData();
        if (isNetworkAvailable) {
            fetchAccommodations();
        } else {
            loadAccomList();
            callback.onNetworkError();
        }
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public List<Accommodation> getAccomList(){
        return accomListFilter;
    }
    public void filterAccoms(String newText, int minPrice, int maxPrice) {
        accomListFilter= new ArrayList<>();
        String query = newText.toLowerCase();
        for (Accommodation accommodation: accomListOrigin){
            String name = accommodation.getName().toLowerCase();
            String address = accommodation.getAddress().toLowerCase();
            if ((name.contains(query) || address.contains(query))
                    && accommodation.getPrice() > minPrice
                    && accommodation.getPrice() < maxPrice){
                accomListFilter.add(accommodation);
            }
        }
        onListChange.postValue(true);
    }
    public void handleSearchByDay(List<CalendarDay> selectedDates, CallbackHelper callback){
        for (Accommodation accommodation: getAccomList()){
            addAccommodationFirestore(accommodation);
        }
        if (selectedDates.size() >= 2) {
            isLoading.setValue(true);
            Date startDate = convertToDate(selectedDates.get(0));
            Date endDate = convertToDate(selectedDates.get(selectedDates.size() - 1));
            for (int i = 0; i < getAccomList().size(); i++){
                int finalI = i;
                getFreeroom(getAccomList().get(finalI).getAccommodationId(), startDate, endDate, new CallbackHelper() {
                    @Override
                    public void onDataReceived(int freeRoom) {
                        setCurrentFreeRoom(finalI, freeRoom);
                        callback.onComplete();
                    }
                });
            }
        }else {
            callback.onDateError();
        }
    }
    public LiveData<String> getErrorLiveData() {
        return error;
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
    public void setCurrentCity(City currentCity){
        this.currentCity = currentCity;
    }
    public void setPrice(String price){
        this.price.set(price);
    }
    private void setCurrentFreeRoom(int i, int freeRoom){
        accomListFilter.get(i).setCurrentFreeroom(freeRoom);
    }
    private void fetchAccommodations(){
        accomRepo.fetchAccommodations(currentCity.getIdCity(), new CallbackHelper() {
            @Override
            public void onStart() {
                loadAccomList();
                if (getAccomList().size() == 0){
                    isLoading.setValue(true);
                }
            }
            @Override
            public void onComplete() {
                isLoading.setValue(false);
                loadAccomList();
            }
            @Override
            public void onError(ErrorResponse errorResponse) {
                error.postValue(errorResponse.getMessage());
            }
        }).subscribe();
    }
    private void loadAccomList(){
        RealmResults<Accommodation> accoms = accomRepo.getAccomsByCity(currentCity.getIdCity());
        accomListOrigin = realmResultToList(accoms);
        accomListFilter = accomListOrigin;
        onListChange.postValue(true);
    }
    private List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm){
        return accomRepo.realmResultToList(accomRealm);
    }
    private void clearErrorLiveData() {
        error.setValue(null); // Gán giá trị null cho LiveData
    }
    private void addAccommodationFirestore(Accommodation accommodation) {
        firestoreDataManager.addAccommodation(accommodation);
    }
    private Date convertToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay());
        return calendar.getTime();
    }

    private void getFreeroom(String accommodationId, Date startDate, Date endDate, CallbackHelper callback) {
        Accommodation accommodation = accomRepo.getAccomnById(accommodationId);
        int totalRoom = accommodation.getFreeroom();
        firebaseBookingRepo.getBookedRoomByTime(accommodationId, startDate, endDate, new CallbackHelper() {
            @Override
            public void onDataReceived(int bookedRoom) {
                int freeRoom = totalRoom - bookedRoom;
                callback.onDataReceived(freeRoom);
            }
        });
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        clearErrorLiveData();
    }
}
