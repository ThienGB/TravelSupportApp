package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserHomeBinding;
import com.example.mapdemo.ui.adapter.ImageHomeAdapter;
import com.example.mapdemo.ui.adapter.IndicatorAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserHomeActivity extends BaseActivity {
    ActivityUserHomeBinding binding;
    FirebaseAuth firebaseAuth;
    private static final String SHARED_PREFS="sharePrefs";
    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPreferences;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_home);
        loginViewModel = getViewModel(LoginViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        loadData();
        setUpSlideImage();
        addEvents();
    }
    private void addEvents(){
        binding.btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, UserBookingListActivity.class);
            startActivity(intent);
        });
        binding.btnResearch.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, UserSelectCountryActivity.class);
            startActivity(intent);
        });
        binding.btnFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, UserFavoriteListActivity.class);
            startActivity(intent);
        });
        binding.btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void setUpSlideImage(){
        handler = new Handler();



        List<String> imageUrls = Arrays.asList(
                "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/anh-phong-canh-dep-22.jpg",
                "https://gcs.tripi.vn/public-tripi/tripi-feed/img/474103DrA/anh-phong-canh-dep-va-nen-tho_093817387.jpg",
                "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/anh-dep-02.jpg.webp",
                "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2020/03/bien-my-khe.jpg"
        );
        ImageHomeAdapter adapter = new ImageHomeAdapter(this, imageUrls);
        binding.vwpImageHome.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvIndicator.setLayoutManager(layoutManager);
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, imageUrls.size());
        binding.rvIndicator.setAdapter(indicatorAdapter);
        binding.vwpImageHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                indicatorAdapter.setCurrentPosition(position);
            }
        });
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == adapter.getItemCount() - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                binding.vwpImageHome.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
    private void loadData(){
        firebaseAuth = FirebaseAuth.getInstance();
        binding.txvTenHS.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        loginViewModel.signOut(sharedPreferences, this);
        showToast("Log out successfully");
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}