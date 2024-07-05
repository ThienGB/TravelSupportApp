package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.data.ActionHelper.ACTION_EDIT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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
import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserCityListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserCityListActivity extends AppCompatActivity {
    private CityAdapter cityAdapter;
    private ActivityUserCityListBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<City> cities;
    @Inject
    ViewModelFactory viewModelFactory;
    private UserCityListViewModel userCityListViewModel;
    private int currentAction = ACTION_EDIT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_city_list);
        initInjec();
        fetchData();
        setUpRecycleView();
        handleBack();

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        realmHelper.openRealm();
        userCityListViewModel = new ViewModelProvider(this, viewModelFactory).get(UserCityListViewModel.class);
        binding.setViewModel(userCityListViewModel);
        binding.setLifecycleOwner(this);
    }
    private void fetchData(){
        userCityListViewModel.setIsLoading(true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            userCityListViewModel.fetchCities();
        } else {
            Toast.makeText(this, "Không có kết nối mạng, dữ liệu có thể không chính xác", Toast.LENGTH_SHORT).show();
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
            }
        }, new CityAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(City city) {
            }
        });
        cities = userCityListViewModel.getCities();
        cities.addChangeListener(results -> {
            cityAdapter.submitList(cities);
            cityAdapter.notifyDataSetChanged();
        });
        binding.rcvCity.setAdapter(cityAdapter);
        cityAdapter.submitList(cities);
    }
    private void handleBack(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCityListActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.closeRealm();
    }
}