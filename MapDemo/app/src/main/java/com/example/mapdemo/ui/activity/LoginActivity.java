package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;

import androidx.databinding.DataBindingUtil;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityLoginBinding;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 9001;
    private LoginViewModel loginViewModel;
    private static final String SHARED_PREFS="sharePrefs";
    private SharedPreferences sharedPreferences;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = getViewModel(LoginViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        if (loginViewModel.checkIsLogin(sharedPreferences, this)){
            showToast("Logged in successfully, Welcome back!");
            finish();
        }
        addEvents();
    }
    private void addEvents(){
        loginViewModel.isLogin.observe(this, isLogin -> {
            if (isLogin != null) {
                loginViewModel.setIsLogin(sharedPreferences, this);
                updateUI(isLogin);
            }
        });
        loginViewModel.errorLiveData.observe(this, error -> {
            if (error != null) {
                showToast(error);
            }
        });
        binding.btnSignInGoogle.setOnClickListener(v -> {
            Intent signInIntent = loginViewModel.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        binding.btnSignUpEmail.setOnClickListener(v -> {
            Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        binding.btnLogin.setOnClickListener(v -> {
            if (CheckAllFields()) {
                loginViewModel.handleLogin(Objects.requireNonNull(binding.edtEmail.getText()).toString(),
                        Objects.requireNonNull(binding.edtPassword.getText()).toString(),
                        LoginActivity.this, () -> {
                            loginViewModel.setIsLogin(sharedPreferences, this);
                            finish();
                        });
            }
        });
    }
    private boolean CheckAllFields() {
        if (binding.edtEmail.length() == 0) {
            binding.edtEmail.setError("This field is required");
            return false;
        }

        if (binding.edtPassword.length() == 0) {
            binding.edtPassword.setError("This field is required");
            return false;
        }

        if (binding.edtPassword.length() < 6) {
            binding.edtPassword.setError("Password must be minimum 6 characters");
            return false;
        }
        if (Objects.requireNonNull(binding.edtEmail.getText()).toString().contains(" ")) {
            binding.edtEmail.setError("Spaces are not allowed");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            binding.edtEmail.setError("Invalid email address");
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            loginViewModel.handleSignInResult(data, this);
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
