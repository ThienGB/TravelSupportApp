package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserCityListBinding;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;

public class UserCityListActivity extends BaseActivity {
    private CityAdapter cityAdapter;
    private ActivityUserCityListBinding binding;
    private UserCityListViewModel userCityLstVm;
    private int countryCode;
    private Observer<String> errorObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_city_list);
        getDataFromIntent();
        userCityLstVm = getViewModel(UserCityListViewModel.class);
        binding.setViewModel(userCityLstVm);
        binding.setLifecycleOwner(this);
        fetchData();
        setUpRecycleView();
        addEvents();
    }
    private void getDataFromIntent(){
        Intent intent = getIntent();
        countryCode = intent.getIntExtra("countryCode", 1);
    }
    private void fetchData(){
        if (isNetworkAvailable()) {
            userCityLstVm.fetchCities(countryCode);
        } else {
            userCityLstVm.loadCityList();
            showToast("No internet, data may be outdated");
        }
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 3));
        cityAdapter = new CityAdapter(city -> {
            Intent intent = new Intent(UserCityListActivity.this, UserAccomListCityActivity.class);
            intent.putExtra("idCity", city.getIdCity());
            intent.putExtra("name", city.getName());
            startActivity(intent);
            finish();
        }, city -> {
        });
        binding.rcvCity.setAdapter(cityAdapter);
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            if (errorObserver != null) {
                userCityLstVm.getErrorLiveData().removeObserver(errorObserver);
            }
            userCityLstVm.clearErrorLiveData();
            Intent intent = new Intent(UserCityListActivity.this, UserSelectCountryActivity.class);
            startActivity(intent);
            finish();
        });
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            private final Handler handler = new Handler();
            private Runnable runnable;
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                runnable = () -> {
                    userCityLstVm.filterCities(newText);
                    updateStatus();
                };
                handler.postDelayed(runnable, 400);
                return true;
            }
        });
        errorObserver = error -> {
            if (error != null) {
                Intent intent = new Intent(UserCityListActivity.this, UserSelectCountryActivity.class);
                intent.putExtra("error", error);
                startActivity(intent);
                finish();
            }
        };
        userCityLstVm.getErrorLiveData().observeForever(errorObserver);
        userCityLstVm.getOnListChange().observe(this, onListChange-> cityAdapter.submitList(userCityLstVm.getCitiList()));
    }
    private void updateStatus(){
        if (userCityLstVm.getCitiList().size() == 0){
            binding.txvInfor.setVisibility(View.VISIBLE);
        }else {
            binding.txvInfor.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.btnBack.callOnClick();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (errorObserver != null) {
            userCityLstVm.getErrorLiveData().removeObserver(errorObserver);
        }userCityLstVm.getOnListChange().removeObservers(this);
        userCityLstVm.clearErrorLiveData();
    }
}