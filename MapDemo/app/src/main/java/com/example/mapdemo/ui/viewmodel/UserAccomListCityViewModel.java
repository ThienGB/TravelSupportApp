package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.helper.LoadingHelper;
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
    List<Accommodation> accomListFilter;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();
    public City currentCity;

    @Inject
    public UserAccomListCityViewModel(AccommodationRepository accomRepo,
                                      FirebaseBookingRepository firebaseBookingRepo,
                                      FirestoreDataManager firestoreDataManager){
        this.accomRepo = accomRepo;
        this.firebaseBookingRepo = firebaseBookingRepo;
        this.firestoreDataManager = firestoreDataManager;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchAccommodations(){
        accomRepo.fetchAccommodations(currentCity.getIdCity(), new LoadingHelper() {
            @Override
            public void onLoadingStarted() {
                loadAccomList();
                if (getAccomList().size() == 0){
                    isLoading.setValue(true);
                }
            }
            @Override
            public void onLoadingFinished() {
                isLoading.setValue(false);
                loadAccomList();
            }
        }, new CallbackHelper() {
            @Override
            public void onError(ErrorResponse errorResponse) {
                error.postValue(errorResponse.getMessage());
            }
        }).subscribe();
    }
    public void loadAccomList(){
        RealmResults<Accommodation> accoms = accomRepo.getAccomsByCity(currentCity.getIdCity());
        accomListOrigin = realmResultToList(accoms);
        accomListFilter = accomListOrigin;
        onListChange.postValue(true);
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
    public Date convertToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay()); // Chú ý tháng trong Calendar bắt đầu từ 0
        return calendar.getTime();
    }

    public void getFreeroom(String accommodationId, Date startDate, Date endDate, CallbackHelper callback) {
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
    public LiveData<String> getErrorLiveData() {
        return error;
    }

    public List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm){
        return accomRepo.realmResultToList(accomRealm);
    }
    public void clearErrorLiveData() {
        error.setValue(null); // Gán giá trị null cho LiveData
    }

    public void addAccommodationFirestore(Accommodation accommodation) {
        firestoreDataManager.addAccommodation(accommodation);
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
    public void setCurrentFreeRoom(int i, int freeRoom){
        accomListFilter.get(i).setCurrentFreeroom(freeRoom);
    }
    public void setCurrentCity(City currentCity){
        this.currentCity = currentCity;
    }
}
