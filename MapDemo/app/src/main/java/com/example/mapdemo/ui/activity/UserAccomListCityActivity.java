package com.example.mapdemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

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

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserAccomListCityActivity extends AppCompatActivity {

    private AccommodationAdapter accomAdapter;
    private ActivityUserAccomListCityBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<Accommodation> accoms;
    List<Accommodation> accomList;
    List<Accommodation> filterAccoms;
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
            accomList = realmHelper.getRealm().copyFromRealm(accoms);
            accomAdapter.submitList(accomList);
            accomAdapter.notifyDataSetChanged();
        });
        binding.rcvCity.setAdapter(accomAdapter);
        accomAdapter.submitList(accomList);
    }
    private void addEvents(){
        final int[] minPrice = {0};
        final int[] maxPrice = {Integer.MAX_VALUE};
        final String[] query = {""};
        binding.searchView.setQueryHint("Find by name or address");
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAccomListCityActivity.this, UserCityListActivity.class);
                startActivity(intent);
            }
        });
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.layFilter.getVisibility() == View.VISIBLE) {
                    binding.layFilter.setVisibility(View.GONE);
                } else {
                    binding.layFilter.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.rsbPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrice[0] = 0;
                maxPrice[0] = (progress);
                String budgetText = "VND " + NumberFormat.getInstance().format(0) + " - VND " + NumberFormat.getInstance().format(maxPrice[0]);
                binding.txvRangePrice.setText(budgetText);
                filterAccoms = userAccomListCityViewModel.filterAccoms(accomList, query[0] ,minPrice[0], maxPrice[0]);
                reLoad();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query[0] = newText;
                filterAccoms = userAccomListCityViewModel.filterAccoms(accomList, query[0] ,minPrice[0], maxPrice[0]);
                reLoad();
                return true;
            }
        });
    }
    private void reLoad(){
        accomAdapter.submitList(filterAccoms);
        if (filterAccoms.size() == 0){
            binding.txvInfor.setVisibility(View.VISIBLE);
        }else {
            binding.txvInfor.setVisibility(View.GONE);
        }
        accomAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        accoms.removeAllChangeListeners();
        realmHelper.closeRealm();
    }
}