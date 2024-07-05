package com.example.mapdemo.ui.activity;

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
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserAccomListCityBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserAccomListCityActivity extends AppCompatActivity {

    private AccommodationAdapter accomAdapter;
    private ActivityUserAccomListCityBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<Accommodation> accoms;
    @Inject
    ViewModelFactory viewModelFactory;
    private UserAccomListCityViewModel userAccomListCityViewModel;
    private City currentCity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_accom_list_city);

        initInjec();
        fetchData();
        getDataFromIntent();
        setUpRecycleView();
        addEvents();
    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idCity = i.getStringExtra("idCity");
        String name = i.getStringExtra("name");
        currentCity = new City(idCity, name);
    }
    private void fetchData(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            userAccomListCityViewModel.fetchAccommodations();
        } else {
            Toast.makeText(this, "Không có kết nối mạng, dữ liệu có thể không chính xác", Toast.LENGTH_SHORT).show();
        }

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        realmHelper.openRealm();
        userAccomListCityViewModel = new ViewModelProvider(this, viewModelFactory).get(UserAccomListCityViewModel.class);
        binding.setViewModel(userAccomListCityViewModel);
        binding.setLifecycleOwner(this);
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 2));
        accoms = userAccomListCityViewModel.getAccomsByCityId(currentCity.getIdCity());
        accomAdapter = new AccommodationAdapter(new AccommodationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                Intent intent = new Intent(UserAccomListCityActivity.this, AccomInforActivity.class);
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameAccom", accommodation.getName());
                startActivity(intent);
            }

        }, new AccommodationAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Accommodation accommodation) {
            }
        });
        accoms.addChangeListener(accommodations -> {
            accomAdapter.submitList(accoms);
            accomAdapter.notifyDataSetChanged();
        });
        binding.rcvCity.setAdapter(accomAdapter);
        accomAdapter.submitList(accoms);
        accomAdapter.notifyDataSetChanged();
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAccomListCityActivity.this, UserCityListActivity.class);
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