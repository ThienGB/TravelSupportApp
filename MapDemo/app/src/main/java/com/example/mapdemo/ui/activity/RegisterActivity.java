package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityRegisterBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.example.mapdemo.ui.viewmodel.RegisterViewModel;

import javax.inject.Inject;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    @Inject
    MyViewModelFactory viewModelFactory;
    private RegisterViewModel registerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_register);
        initInjec();
        addEvents();
    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        registerViewModel = new ViewModelProvider(this, viewModelFactory).get(RegisterViewModel.class);
    }
    private void addEvents(){
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFields()){
                    registerViewModel.handleSignUp(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString(),
                            binding.edtName.getText().toString(),RegisterActivity.this);
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