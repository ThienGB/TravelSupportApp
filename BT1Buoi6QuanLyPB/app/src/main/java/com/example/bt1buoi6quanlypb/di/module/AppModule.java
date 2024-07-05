package com.example.bt1buoi6quanlypb.di.module;

import android.content.Context;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.data.repository.PhongBanRepository;
import com.example.bt1buoi6quanlypb.data.repository.PhongBanRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

    @Provides
    @Singleton
    RealmHelper provideRealmHelper(){
        return new RealmHelper(context);
    }

    @Provides
    @Singleton
    PhongBanRepository providePhongBanRepository(RealmHelper realmHelper){
        return new PhongBanRepositoryImpl(realmHelper);
    }
}
