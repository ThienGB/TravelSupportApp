package com.example.bt1buoi6quanlypb.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private RealmHelper realmHelper;
    public ViewModelFactory(RealmHelper realmHelper) {
        this.realmHelper = realmHelper;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhongBanViewModel.class)) {
            return (T) new PhongBanViewModel(realmHelper);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
