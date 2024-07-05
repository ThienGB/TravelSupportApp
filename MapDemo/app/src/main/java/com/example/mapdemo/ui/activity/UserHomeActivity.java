package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserHomeActivity extends AppCompatActivity {
    ActivityUserHomeBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_home);
        loadData();
        addEvents();
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
                Intent intent = new Intent(UserHomeActivity.this, UserCityListActivity.class);
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
    }
    private void loadData(){
        firebaseAuth = FirebaseAuth.getInstance();
        binding.txvTenHS.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
    }
}