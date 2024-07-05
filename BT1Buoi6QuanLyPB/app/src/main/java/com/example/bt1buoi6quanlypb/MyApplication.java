package com.example.bt1buoi6quanlypb;

import android.app.Application;

import com.example.bt1buoi6quanlypb.di.component.AppComponent;
import com.example.bt1buoi6quanlypb.di.component.DaggerAppComponent;
import com.example.bt1buoi6quanlypb.di.module.AppModule;

public class MyApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
    public AppComponent getAppComponent(){
        return appComponent;
    }
}
