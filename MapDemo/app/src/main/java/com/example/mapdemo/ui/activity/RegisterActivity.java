package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.library.baseAdapters.BR;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityRegisterBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.RegisterViewModel;
import java.util.Objects;

public class RegisterActivity extends BaseActivity<RegisterViewModel, ActivityRegisterBinding> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addEvents();
    }

    @Override
    protected Class<RegisterViewModel> getViewModelClass() {
        return RegisterViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
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
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        binding.btnSignUp.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            String name = Objects.requireNonNull(binding.edtName.getText()).toString();
            viewModel.handleSignUp(email, password, name, RegisterActivity.this, new CallbackHelper() {
                @Override
                public void onEmailError(String message) {
                    binding.edtEmail.setError(message);
                }
                @Override
                public void onPasswordError(String message) {
                    binding.edtPassword.setError(message);
                }
            });
        });
    }
}