package com.example.mapdemo.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapdemo.data.RealmHelper;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final RealmHelper realmHelper;

    public ViewModelFactory(RealmHelper realmHelper) {
        this.realmHelper = realmHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminCityListViewModel.class)) {
            return (T) new AdminCityListViewModel(realmHelper);
        } else if (modelClass.isAssignableFrom(AdminListAccomCityViewModel.class)) {
            return (T) new AdminListAccomCityViewModel(realmHelper);
        } else if (modelClass.isAssignableFrom(AccomInforViewModel.class)) {
            return (T) new AccomInforViewModel(realmHelper);
        } else if (modelClass.isAssignableFrom(UserCityListViewModel.class)) {
            return (T) new UserCityListViewModel(realmHelper);
        } else if (modelClass.isAssignableFrom(UserAccomListCityViewModel.class)) {
            return (T) new UserAccomListCityViewModel(realmHelper);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
