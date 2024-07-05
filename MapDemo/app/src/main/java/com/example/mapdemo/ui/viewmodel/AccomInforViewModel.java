package com.example.mapdemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.BookingRepositoryImpl;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FavoriteRepositoryImpl;

public class AccomInforViewModel extends ViewModel {
    private AccommodationRepository accomRepo;
    private FavoriteRepository favoriteRepo;
    private BookingRepository bookingRepo;
    private MutableLiveData<Boolean> isFavorite = new MutableLiveData<>(false);

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
}
