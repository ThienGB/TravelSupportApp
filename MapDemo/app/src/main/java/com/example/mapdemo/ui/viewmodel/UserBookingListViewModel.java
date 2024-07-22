package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.BookingRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserBookingListViewModel extends ViewModel {
    private final BookingRepository bookingRepo;
    private final AccommodationRepository accomRepo;
    private final MutableLiveData<Boolean> onListChange = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;
    private List<Booking> bookedList;
    @Inject
    public UserBookingListViewModel(BookingRepository bookingRepo, AccommodationRepository accomRepo,
                                    FirebaseAuth firebaseAuth){
        this.accomRepo = accomRepo;
        this.bookingRepo = bookingRepo;
        this.firebaseAuth = firebaseAuth;
    }
    public void loadBookingByIdUser() {
        bookedList = realToList(bookingRepo.getBookingByIdUser(
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()));
        onListChange.postValue(true);
    }
    public Accommodation getAccomById(String idAccom){
        return accomRepo.getAccomnById(idAccom);
    }
    public List<Booking> getBookedList(){
        return bookedList;
    }
    public MutableLiveData<Boolean> getOnListChange(){
        return onListChange;
    }
    private List<Booking> realToList(RealmResults<Booking> bookedRealmResult){
        return bookingRepo.realmToList(bookedRealmResult);
    }
}
