package com.example.mapdemo.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    @Inject
    protected MyViewModelFactory viewModelFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
    }
    private void initInject() {
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent = mainApplication.getActivityComponent();
        activityComponent.inject(this);
    }
    protected <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return new ViewModelProvider(this, viewModelFactory).get(modelClass);
    }
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
