package com.example.mapdemo.di.component;

import com.example.mapdemo.di.module.ActivityModule;
import com.example.mapdemo.di.module.AppModule;
import com.example.mapdemo.ui.activity.AccomInforActivity;
import com.example.mapdemo.ui.activity.LoginActivity;
import com.example.mapdemo.ui.activity.RegisterActivity;
import com.example.mapdemo.ui.activity.UserAccomListCityActivity;
import com.example.mapdemo.ui.activity.UserBookingListActivity;
import com.example.mapdemo.ui.activity.UserCityListActivity;
import com.example.mapdemo.ui.activity.UserFavoriteListActivity;
import com.example.mapdemo.ui.activity.UserHomeActivity;
import com.example.mapdemo.ui.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, AppModule.class})
public interface ActivityComponent {
    void inject(UserCityListActivity userCityListActivity);
    void inject(LoginActivity loginActivity);
    void inject(AccomInforActivity accomInforActivity);
    void inject(UserBookingListActivity userBookingListActivity);
    void inject(RegisterActivity registerActivity);
    void inject(UserAccomListCityActivity userAccomListCityActivity);
    void inject(UserFavoriteListActivity userFavoriteListActivity);

    void inject(UserHomeActivity userHomeActivity);
}
