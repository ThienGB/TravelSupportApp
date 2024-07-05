package com.example.bt1buoi6quanlypb.di.module;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.ui.viewmodel.ViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    @Provides
    ViewModelFactory provideViewModelFatory(RealmHelper realmHelper){
        return new ViewModelFactory(realmHelper);
    }
}
