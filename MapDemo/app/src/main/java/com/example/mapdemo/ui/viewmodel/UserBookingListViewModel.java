package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.BookingRepository;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserBookingListViewModel extends ViewModel {
    private final BookingRepository bookingRepo;
    private final AccommodationRepository accomRepo;
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();
    public List<Booking> bookedList;
    @Inject
    public UserBookingListViewModel(BookingRepository bookingRepo, AccommodationRepository accomRepo){
        this.accomRepo = accomRepo;
        this.bookingRepo = bookingRepo;
    }
    public void loadBookingByIdUser(String idUser) {
        bookedList = realToList(bookingRepo.getBookingByIdUser(idUser));
        onListChange.postValue(true);
    }
    public Accommodation getAccomById(String idAccom){
        return accomRepo.getAccomnById(idAccom);
    }
    private List<Booking> realToList(RealmResults<Booking> bookedRealmResult){
        return bookingRepo.realmToList(bookedRealmResult);
    }
    public List<Booking> getBookedList(){
        return bookedList;
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
}
