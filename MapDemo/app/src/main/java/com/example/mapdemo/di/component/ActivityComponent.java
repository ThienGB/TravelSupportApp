package com.example.mapdemo.di.component;

import com.example.mapdemo.di.module.ActivityModule;
import com.example.mapdemo.di.module.AppModule;
import com.example.mapdemo.ui.activity.AccomInforActivity;
import com.example.mapdemo.ui.activity.AdminAccomListActivity;
import com.example.mapdemo.ui.activity.AdminAccomListCityActivity;
import com.example.mapdemo.ui.activity.AdminCityListActivity;
import com.example.mapdemo.ui.activity.UserAccomListCityActivity;
import com.example.mapdemo.ui.activity.UserCityListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, AppModule.class})
public interface ActivityComponent {
    void inject(AdminCityListActivity activity);
    void inject(AdminAccomListCityActivity activity);
    void inject(AdminAccomListActivity adminAccomListActivity);
    void inject(AccomInforActivity accomInforActivity);

    void inject(UserCityListActivity userCityListActivity);

    void inject(UserAccomListCityActivity userAccomListCityActivity);
}