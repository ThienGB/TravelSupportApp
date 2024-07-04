package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityAdminHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_home);
        addEvents();
    }

    private void addEvents(){
        binding.btnAccomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminAccomListActivity.class);
                startActivity(intent);
            }
        });
        binding.btnCityList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCityListActivity.class);
                startActivity(intent);
            }
        });
    }
}