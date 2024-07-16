package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityLoginBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    @Inject
    MyViewModelFactory viewModelFactory;
    private LoginViewModel loginViewModel;
    public static final String SHARED_PREFS="sharePrefs";
    private ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initInjec();
        addEvents();
        loginViewModel.signOut();

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
    }
    private void addEvents(){
        loginViewModel.userLiveData.observe(this, user -> {
            if (user != null) {
                updateUI(user);
                progressDialog.dismiss();
            } else {
                updateUI(null);
            }
        });
        loginViewModel.errorLiveData.observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = loginViewModel.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

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
                            finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login...");
        progressDialog.show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            loginViewModel.handleSignInResult(data);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Logged in successfully",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Please sign in to continue.", Toast.LENGTH_SHORT).show();
        }
    }
}
