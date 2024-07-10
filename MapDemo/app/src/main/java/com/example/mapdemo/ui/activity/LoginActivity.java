package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.databinding.ActivityLoginBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    @Inject
    RealmHelper realmHelper;
    @Inject
    ViewModelFactory viewModelFactory;
    private LoginViewModel loginViewModel;
    public static final String SHARED_PREFS="sharePrefs";
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initInjec();
        addEvents();

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
    }
    private void addEvents(){
        binding.btnSignUpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFields()) {
                    loginViewModel.handleLogin(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString(),
                            LoginActivity.this, new LoginViewModel.UploadCallback() {
                        @Override
                        public void onUploadComplete() {
                            Intent intent= new Intent(LoginActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putString("name","true");
                            editor.apply();
                        }
                    });
                }
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
        if (binding.edtEmail.getText().toString().contains(" ")) {
            binding.edtEmail.setError("Spaces are not allowed");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            binding.edtEmail.setError("Invalid email address");
            return false;
        }
        return true;
    }
}
