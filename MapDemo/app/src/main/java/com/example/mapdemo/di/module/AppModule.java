package com.example.mapdemo.di.module;

import android.app.Application;
import android.content.Context;

import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.BookingDao;
import com.example.mapdemo.data.local.dao.BookingDaoImpl;
import com.example.mapdemo.data.local.dao.CitiyDaoImpl;
import com.example.mapdemo.data.local.dao.CityDao;
import com.example.mapdemo.data.local.dao.FavoriteDao;
import com.example.mapdemo.data.local.dao.FavoriteDaoImpl;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.remote.firestore.FirestoreDataManagerImpl;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.BookingRepositoryImpl;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.CityRepositoryImpl;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FavoriteRepositoryImpl;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepositoryImpl;
import com.example.mapdemo.helper.RealmHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context){
        this.context = context;
    }
    @Singleton
    @Provides
    Context provideContext(){
        return context;
    }
    @Singleton
    @Provides
    Application provideApplication(){
        return (Application) context.getApplicationContext();
    }
    @Singleton
    @Provides
    RealmHelper provideRealmHelper(){
        return new RealmHelper(context);
    }

    @Provides
    AccommodationDao provideAccommodationDao(RealmHelper realmHelper) {
        return new AccommodationDaoImpl(realmHelper);
    }
    @Provides
    AccommodationRepository provideAccommodationRepository(AccommodationDao accommodationDao){
        return new AccommodationRepositoryImpl(accommodationDao);
    }
    @Provides
    CityDao provideCityDao(RealmHelper realmHelper) {
        return new CitiyDaoImpl(realmHelper);
    }

    @Provides
    CityRepository provideCityRepository(CityDao cityDao) {
        return new CityRepositoryImpl(cityDao);
    }

    @Provides
    FavoriteDao provideFavoriteDao(RealmHelper realmHelper) {
        return new FavoriteDaoImpl(realmHelper);
    }

    @Provides
    FavoriteRepository provideFavoriteRepository(FavoriteDao favoriteDao, AccommodationDao accommodationDao) {
        return new FavoriteRepositoryImpl(favoriteDao, accommodationDao);
    }

    @Provides
    BookingDao provideBookingDao(RealmHelper realmHelper) {
        return new BookingDaoImpl(realmHelper);
    }

    @Provides
    BookingRepository provideBookingRepository(BookingDao bookingDao, AccommodationDao accommodationDao) {
        return new BookingRepositoryImpl(bookingDao, accommodationDao);
    }


    @Provides
    String provideFirebaseInstance() {
        String firebaseInstance = "https://mapdemo-b04e7-default-rtdb.asia-southeast1.firebasedatabase.app";
        return firebaseInstance;
    }
    @Provides
    FirebaseBookingRepository provideFirebaseBookingRepository(String firebaseInstance) {
        return new FirebaseBookingRepositoryImpl(firebaseInstance);
    }
    @Provides
    FirebaseAuth provideFirebaseAuth() {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        return firebaseAuth;
    }
    @Provides
    FirebaseFirestore provideFirebaseFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db;
    }
    @Provides
    FirestoreDataManager provideFirestoreManager(FirebaseFirestore db, List<ListenerRegistration> listenerRegistrations) {
        return new FirestoreDataManagerImpl(db, listenerRegistrations);
    }
    @Provides
    List<ListenerRegistration> provideListenerRegistrations(){
        return new ArrayList<>();
    }
}
