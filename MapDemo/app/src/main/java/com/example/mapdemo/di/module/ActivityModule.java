package com.example.mapdemo.di.module;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    @Provides
    ViewModelFactory provideViewModelFactory(RealmHelper realmHelper){
        return new ViewModelFactory(realmHelper);
    }
}
