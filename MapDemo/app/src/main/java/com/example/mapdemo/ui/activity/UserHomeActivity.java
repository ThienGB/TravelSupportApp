package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserHomeBinding;

public class UserHomeActivity extends AppCompatActivity {
    ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_home);
        addEvents();
    }
    private void addEvents(){
        binding.btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, UserCityListActivity.class);
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
    }
}