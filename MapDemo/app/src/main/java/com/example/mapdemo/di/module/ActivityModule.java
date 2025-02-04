package com.example.mapdemo.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapdemo.data.remote.firestore.FirestoreDataManager;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.BookingRepository;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.FavoriteRepository;
import com.example.mapdemo.data.repository.FirebaseBookingRepository;
import com.example.mapdemo.di.ViewModelKey;
import com.example.mapdemo.ui.viewmodel.AccomInforViewModel;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.example.mapdemo.ui.viewmodel.RegisterViewModel;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.example.mapdemo.ui.viewmodel.UserBookingListViewModel;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;
import com.example.mapdemo.ui.viewmodel.UserFavoriteListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ActivityModule {
    @Provides
    @Singleton
    ViewModelProvider.Factory provideMyViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new MyViewModelFactory(providerMap);
    }
    @Provides
    UserCityListViewModel provideUserCityListViewModelWithRepo(CityRepository cityRepository) {
        return new UserCityListViewModel(cityRepository);
    }
    @Provides
    @IntoMap
    @ViewModelKey(UserCityListViewModel.class)
    ViewModel provideUserCityListViewModel(UserCityListViewModel viewModel) {
        return viewModel;
    }

    @Provides
    UserAccomListCityViewModel provideUserAccomListCityViewModelWithRepo(AccommodationRepository accommodationRepository,
                                                                         FirebaseBookingRepository firebaseBookingRepository,
                                                                         FirestoreDataManager firestoreDataManager) {
        return new UserAccomListCityViewModel(accommodationRepository, firebaseBookingRepository, firestoreDataManager);
    }
    @Provides
    @IntoMap
    @ViewModelKey(UserAccomListCityViewModel.class)
    ViewModel provideUserAccomListCityViewModel(UserAccomListCityViewModel viewModel) {
        return viewModel;
    }
    @Provides
    UserFavoriteListViewModel provideUserFavoriteListViewModelWithRepo(FavoriteRepository favoriteRepository,
                                                                       FirestoreDataManager firestoreDataManager,
                                                                       AccommodationRepository accommodationRepo,
                                                                       FirebaseAuth firebaseAuth) {
        return new UserFavoriteListViewModel(favoriteRepository,
                firestoreDataManager, accommodationRepo, firebaseAuth);
    }
    @Provides
    @IntoMap
    @ViewModelKey(UserFavoriteListViewModel.class)
    ViewModel provideUserFavoriteListViewModel(UserFavoriteListViewModel viewModel) {
        return viewModel;
    }
    @Provides
    UserBookingListViewModel provideUserBookingListViewModelWithRepo(BookingRepository bookingRepo
            , AccommodationRepository accomRepo, FirebaseAuth firebaseAuth) {
        return new UserBookingListViewModel(bookingRepo, accomRepo, firebaseAuth);
    }
    @Provides
    @IntoMap
    @ViewModelKey(UserBookingListViewModel.class)
    ViewModel provideUserBookingListViewModel(UserBookingListViewModel viewModel) {
        return viewModel;
    }
    @Provides
    AccomInforViewModel provideAccomInforViewModelWithRepo(AccommodationRepository accomRepo,
                                                           FavoriteRepository favoriteRepo,
                                                           BookingRepository bookingRepo,
                                                           FirebaseBookingRepository firebaseBookingRepo,
                                                           FirestoreDataManager firestoreDataManager,
                                                           FirebaseAuth firebaseAuth) {
        return new AccomInforViewModel(accomRepo, favoriteRepo, bookingRepo
                ,firebaseBookingRepo,firestoreDataManager, firebaseAuth);
    }
    @Provides
    @IntoMap
    @ViewModelKey(AccomInforViewModel.class)
    ViewModel provideAccomInforViewModel(AccomInforViewModel viewModel) {
        return viewModel;
    }

    @Provides
    LoginViewModel provideLoginViewModelWithRepo(FirebaseAuth firebaseAuth, GoogleSignInClient googleSignInClient) {
        return new LoginViewModel(firebaseAuth, googleSignInClient);
    }
    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    ViewModel provideLoginViewModel(LoginViewModel viewModel) {
        return viewModel;
    }

    @Provides
    RegisterViewModel provideRegisterViewModelWithRepo(FirebaseAuth firebaseAuth) {
        return new RegisterViewModel(firebaseAuth);
    }
    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    ViewModel provideRegisterViewModel(RegisterViewModel viewModel) {
        return viewModel;
    }
}
