package com.example.bt1buoi6quanlypb.di.component;

import com.example.bt1buoi6quanlypb.activity.MainActivity;
import com.example.bt1buoi6quanlypb.di.module.ActivityModule;
import com.example.bt1buoi6quanlypb.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ActivityModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
