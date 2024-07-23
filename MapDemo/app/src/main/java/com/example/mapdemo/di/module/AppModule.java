package com.example.mapdemo.di.module;

import android.app.Application;
import android.content.Context;

import com.example.mapdemo.R;
import com.example.mapdemo.data.local.dao.AccommodationDao;
import com.example.mapdemo.data.local.dao.AccommodationDaoImpl;
import com.example.mapdemo.data.local.dao.BookingDao;
import com.example.mapdemo.data.local.dao.BookingDaoImpl;
import com.example.mapdemo.data.local.dao.CitiyDaoImpl;
import com.example.mapdemo.data.local.dao.CityDao;
import com.example.mapdemo.data.local.dao.FavoriteDao;
import com.example.mapdemo.data.local.dao.FavoriteDaoImpl;
import com.example.mapdemo.data.remote.api.ApiService;
import com.example.mapdemo.data.remote.api.RetrofitClient;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
    AccommodationRepository provideAccommodationRepository(AccommodationDao accommodationDao,
                                                           @Named("accomService") ApiService apiService){
        return new AccommodationRepositoryImpl(accommodationDao, apiService);
    }
    @Provides
    CityDao provideCityDao(RealmHelper realmHelper) {
        return new CitiyDaoImpl(realmHelper);
    }

    @Provides
    CityRepository provideCityRepository(CityDao cityDao, @Named("cityService") ApiService apiService) {
        return new CityRepositoryImpl(cityDao, apiService);
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
        return "https://mapdemo-b04e7-default-rtdb.asia-southeast1.firebasedatabase.app";
    }
    @Provides
    FirebaseBookingRepository provideFirebaseBookingRepository(String firebaseInstance) {
        return new FirebaseBookingRepositoryImpl(firebaseInstance);
    }
    @Provides
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
    @Provides
    FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }
    @Provides
    FirestoreDataManager provideFirestoreManager(FirebaseFirestore db, List<ListenerRegistration> listenerRegistrations) {
        return new FirestoreDataManagerImpl(db, listenerRegistrations);
    }
    @Provides
    List<ListenerRegistration> provideListenerRegistrations(){
        return new ArrayList<>();
    }
    @Provides
    GoogleSignInClient provideGoogleSignInClient(Application application){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(application.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        return GoogleSignIn.getClient(application, gso);
    }

    @Provides
    @Named("accommodation")
    public Retrofit provideRetrofitAccom() {
        return new Retrofit.Builder()
                .baseUrl(RetrofitClient.ACCOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Named("city")
    public Retrofit provideRetrofitCity() {
        return new Retrofit.Builder()
                .baseUrl(RetrofitClient.ACCOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Named("accomService")
    public ApiService provideAccommodationApiService(@Named("accommodation") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Named("cityService")
    public ApiService provideCityApiService(@Named("city") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


}
