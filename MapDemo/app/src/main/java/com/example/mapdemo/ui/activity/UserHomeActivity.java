package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserHomeBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.generated.callback.OnClickListener;
import com.example.mapdemo.ui.adapter.ImageHomeAdapter;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class UserHomeActivity extends AppCompatActivity {
    ActivityUserHomeBinding binding;
    FirebaseAuth firebaseAuth;
    @Inject
    MyViewModelFactory viewModelFactory;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_home);
        initInjec();
        loadData();
        addEvents();

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
    }
    private void addEvents(){
        binding.btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, UserBookingListActivity.class);
                startActivity(intent);
            }
        });
        binding.btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, UserSelectCountryActivity.class);
                startActivity(intent);
            }
        });
        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, UserFavoriteListActivity.class);
                startActivity(intent);
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }
    private void loadData(){
        firebaseAuth = FirebaseAuth.getInstance();
        binding.txvTenHS.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());

        List<String> imageUrls = Arrays.asList(
                "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/anh-phong-canh-dep-22.jpg",
                "https://gcs.tripi.vn/public-tripi/tripi-feed/img/474103DrA/anh-phong-canh-dep-va-nen-tho_093817387.jpg",
                "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/anh-dep-02.jpg.webp",
                "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2020/03/bien-my-khe.jpg"
        );
        ImageHomeAdapter adapter = new ImageHomeAdapter(this, imageUrls);
        binding.vwpImageHome.setAdapter(adapter);
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        loginViewModel.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}