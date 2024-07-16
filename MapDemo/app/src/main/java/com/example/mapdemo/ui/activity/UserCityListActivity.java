package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserCityListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserCityListActivity extends AppCompatActivity {
    private CityAdapter cityAdapter;
    private ActivityUserCityListBinding binding;
    RealmResults<City> cities;
    List<City> cityList;
    @Inject
    MyViewModelFactory viewModelFactory;
    private UserCityListViewModel userCityListViewModel;
    private int countryCode;
    private Observer<ErrorResponse> errorObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_city_list);
        initInjec();
        getDataFromIntent();
        fetchData();
        setUpRecycleView();
        addEvents();
    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        userCityListViewModel = new ViewModelProvider(this, viewModelFactory).get(UserCityListViewModel.class);
        binding.setViewModel(userCityListViewModel);
        binding.setLifecycleOwner(this);
    }
    private void getDataFromIntent(){
        Intent intent = getIntent();
        countryCode = intent.getIntExtra("countryCode", 1);
    }
    private void fetchData(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            userCityListViewModel.fetchCities(countryCode);
        } else {
            Toast.makeText(this, "No internet, data may be outdated", Toast.LENGTH_SHORT).show();
        }
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 3));
        cityAdapter = new CityAdapter(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(City city) {
                Intent intent = new Intent(UserCityListActivity.this, UserAccomListCityActivity.class);
                intent.putExtra("idCity", city.getIdCity());
                intent.putExtra("name", city.getName());
                startActivity(intent);
                finish();
            }
        }, new CityAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(City city) {
            }
        });
        cities = userCityListViewModel.getCities();
        cities.addChangeListener(results -> {
            cityList = userCityListViewModel.realmResultToList(cities);
            cityAdapter.submitList(cityList);
            cityAdapter.notifyDataSetChanged();
        });
        binding.rcvCity.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
        cityAdapter.submitList(cityList);
    }
    private void addEvents(){
        binding.searchView.setQueryHint("Find by city name");
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorObserver != null) {
                    userCityListViewModel.getErrorLiveData().removeObserver(errorObserver);
                }
                userCityListViewModel.clearErrorLiveData();
                Intent intent = new Intent(UserCityListActivity.this, UserSelectCountryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (cityList == null){
                    return false;
                }
                List<City> filterCities = userCityListViewModel.filterCities(cityList, newText);
                reLoad(filterCities);
                return true;
            }
        });
        errorObserver = new Observer<ErrorResponse>() {
            @Override
            public void onChanged(ErrorResponse error) {
                if (error != null) {
                    showErrorDialog(UserCityListActivity.this, error.getMessage());
                    cityList = null;
                    cityAdapter.submitList(cityList);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        };
        userCityListViewModel.getErrorLiveData().observeForever(errorObserver);
    }
    private void reLoad(List<City> filterCities ){
        cityAdapter.submitList(filterCities);
        if (filterCities.size() == 0){
            binding.txvInfor.setVisibility(View.VISIBLE);
        }else {
            binding.txvInfor.setVisibility(View.GONE);
        }
        cityAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (errorObserver != null) {
            userCityListViewModel.getErrorLiveData().removeObserver(errorObserver);
        }
        userCityListViewModel.clearErrorLiveData();
    }
}