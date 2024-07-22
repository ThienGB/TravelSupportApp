package com.example.mapdemo.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public abstract class BaseActivity<T extends ViewModel, V extends ViewDataBinding> extends AppCompatActivity{
    @Inject
    protected MyViewModelFactory viewModelFactory;
    protected T viewModel;
    protected V binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        getViewModel();
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setVariable(getBindingVariable(), viewModel);
        binding.setLifecycleOwner(this);
    }
    private void initInject() {
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent = mainApplication.getActivityComponent();
        injectActivity(activityComponent);
    }
    private void getViewModel() {
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(getViewModelClass());
    }
    protected abstract Class<T> getViewModelClass();
    protected abstract int getLayoutId();
    protected abstract int getBindingVariable();
    protected abstract void injectActivity(ActivityComponent activityComponent);
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
