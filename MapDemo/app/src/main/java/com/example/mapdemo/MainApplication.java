package com.example.mapdemo;

import android.app.Application;

import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.di.component.DaggerActivityComponent;
import com.example.mapdemo.di.module.ActivityModule;
import com.example.mapdemo.di.module.AppModule;


public class MainApplication extends Application {
    private ActivityComponent activityComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        activityComponent = DaggerActivityComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
    public ActivityComponent getActivityComponent(){
        return activityComponent;
    }
}
