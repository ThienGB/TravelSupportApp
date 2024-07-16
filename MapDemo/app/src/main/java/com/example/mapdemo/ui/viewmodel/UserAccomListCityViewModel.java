package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepositoryImpl;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.helper.LoadingHelper;
import com.google.firestore.v1.FirestoreProto;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserAccomListCityViewModel extends ViewModel {
    private AccommodationRepository accomRepo;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private RealmResults<Accommodation> accoms;
    private MutableLiveData<ErrorResponse> error = new MutableLiveData<>();
    private FirebaseBookingRepository firebaseBookingRepo;
    private FirestoreDataManager firestoreDataManager;
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
    public RealmResults<Accommodation> getAccoms(){
        accoms = accomRepo.getAccomList();
        return accoms;
    }
    public void fetchAccommodations(String cityId){
        isLoading.setValue(true);
        accomRepo.fetchAccommodations(cityId, new LoadingHelper() {
            @Override
            public void onLoadingStarted() {
                isLoading.setValue(true);
            }

            @Override
            public void onLoadingFinished() {
                isLoading.setValue(false);
            }
        }, new CallbackHelper() {
            @Override
            public void onError(ErrorResponse errorResponse) {
                error.postValue(errorResponse);
            }
        }).subscribe();
    }
    public RealmResults<Accommodation> getAccomsByCityId(String cityId){
        accoms = accomRepo.getAccomsByCity(cityId);
        return accoms;
    }
    public List<Accommodation> filterAccoms(List<Accommodation> accomList, String newText, int minPrice, int maxPrice) {
        List<Accommodation> filterAccoms= new ArrayList<>();
        String query = newText.toLowerCase();
        for (Accommodation accommodation: accomList){
            String name = accommodation.getName().toLowerCase();
            String address = accommodation.getAddress().toLowerCase();
            if ((name.contains(query) || address.contains(query))
                    && accommodation.getPrice() > minPrice
                    && accommodation.getPrice() < maxPrice){
                filterAccoms.add(accommodation);
            }
        }
        return filterAccoms;
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
    public LiveData<ErrorResponse> getErrorLiveData() {
        return error;
    }

    public List<Accommodation> realmResultToList(RealmResults<Accommodation> accomRealm){
        List<Accommodation> accomList = accomRepo.realmResultToList(accomRealm);
        return accomList;
    }
    public void clearErrorLiveData() {
        error.setValue(null); // Gán giá trị null cho LiveData
    }

    public void addAccommodationFirestore(Accommodation accommodation) {
        firestoreDataManager.addAccommodation(accommodation);
    }
}
