package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mapdemo.BR;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityLoginBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;

import org.jetbrains.annotations.Nullable;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {
    private static final int RC_SIGN_IN = 9001;
    private static final String SHARED_PREFS="sharePrefs";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        if (viewModel.checkIsLogin(sharedPreferences, this)){
            showToast("Logged in successfully, Welcome back!");
            finish();
        }
        addEvents();
    }
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }
    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
    private void addEvents(){
        viewModel.isLogin.observe(this, isLogin -> {
            if (isLogin != null) {
                viewModel.setIsLogin(sharedPreferences, this);
                updateUI(isLogin);
            }
        });
        viewModel.errorLiveData.observe(this, error -> {
            if (error != null) {
                showToast(error);
            }
        });
        binding.btnSignInGoogle.setOnClickListener(v -> {
            Intent signInIntent = viewModel.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        binding.btnSignUpEmail.setOnClickListener(v -> {
            Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        binding.btnLogin.setOnClickListener(v -> viewModel.handleLogin(LoginActivity.this, new CallbackHelper() {
            @Override
            public void onComplete() {
                viewModel.setIsLogin(sharedPreferences, LoginActivity.this);
                finish();
            }
        }));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            viewModel.handleSignInResult(data, this);
        }
    }
    private void updateUI(boolean isLogin) {
        if (isLogin) {
            showToast("Logged in successfully");
            finish();
        } else {
            showToast("Please sign in to continue.");
        }
    }
}
