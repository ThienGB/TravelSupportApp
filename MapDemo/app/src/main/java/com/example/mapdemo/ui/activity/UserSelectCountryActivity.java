package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserSelectCountryBinding;

public class UserSelectCountryActivity extends AppCompatActivity {
    ActivityUserSelectCountryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_user_select_country);
        getDataFormIntent();
        addEvents();
    }
    private void getDataFormIntent(){
        Intent intent = getIntent();
        if (intent.getStringExtra("error") != null){
            showErrorDialog(this, intent.getStringExtra("error"));
        }
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
           Intent intent = new Intent(UserSelectCountryActivity.this, UserHomeActivity.class);
           startActivity(intent);
           finish();
        });
        binding.btnVietnamese.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectCountryActivity.this, UserCityListActivity.class);
            intent.putExtra("countryCode", 1);
            startActivity(intent);
            finish();
        });
        binding.btnChinese.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectCountryActivity.this, UserCityListActivity.class);
            intent.putExtra("countryCode", 2);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.btnBack.callOnClick();
    }
}