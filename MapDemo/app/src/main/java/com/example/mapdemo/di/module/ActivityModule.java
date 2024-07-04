package com.example.mapdemo.di.module;

import com.example.mapdemo.data.RealmHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    @Provides
    ViewModelFactory provideViewModelFactory(RealmHelper realmHelper){
        return new ViewModelFactory(realmHelper);
    }
}
