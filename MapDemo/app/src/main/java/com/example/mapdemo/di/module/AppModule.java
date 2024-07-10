package com.example.mapdemo.di.module;

import android.content.Context;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.repository.AccommodationRepository;
import com.example.mapdemo.data.repository.AccommodationRepositoryImpl;
import com.example.mapdemo.data.repository.CityRepository;
import com.example.mapdemo.data.repository.CityRepositoryImpl;

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
    RealmHelper provideRealmHelper(){
        return new RealmHelper(context);
    }
    @Singleton
    @Provides
    CityRepository provideCityRepository(RealmHelper realmHelper){
        return new CityRepositoryImpl(realmHelper);
    }
    @Singleton
    @Provides
    AccommodationRepository provideAccommodationRepository(RealmHelper realmHelper){
        return new AccommodationRepositoryImpl(realmHelper);
    }
}
