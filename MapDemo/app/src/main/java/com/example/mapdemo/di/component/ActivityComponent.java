package com.example.mapdemo.di.component;

import com.example.mapdemo.di.module.ActivityModule;
import com.example.mapdemo.di.module.AppModule;
import com.example.mapdemo.ui.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, AppModule.class})
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);
}
