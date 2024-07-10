package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserSelectCountryBinding;

public class UserSelectCountryActivity extends AppCompatActivity {
    ActivityUserSelectCountryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_select_country);
        addEvents();
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelectCountryActivity.this, UserCityListActivity.class);
                intent.putExtra("countryCode", 1);
                startActivity(intent);
            }
        });
        binding.btnChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelectCountryActivity.this, UserCityListActivity.class);
                intent.putExtra("countryCode", 2);
                startActivity(intent);
            }
        });
    }
}