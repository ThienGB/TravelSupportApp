package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.databinding.DataBindingUtil;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityRegisterBinding;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.RegisterViewModel;

import java.util.Objects;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = getViewModel(RegisterViewModel.class);
        addEvents();
    }
    private void addEvents(){
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        binding.btnSignUp.setOnClickListener(v -> {
            if (CheckAllFields()){
                registerViewModel.handleSignUp(Objects.requireNonNull(binding.edtEmail.getText()).toString(),
                        Objects.requireNonNull(binding.edtPassword.getText()).toString(),
                        Objects.requireNonNull(binding.edtName.getText()).toString(),RegisterActivity.this);
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
}