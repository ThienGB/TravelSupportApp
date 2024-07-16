package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.api.ErrorResponse;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserAccomListCityBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserAccomListCityActivity extends AppCompatActivity {

    private AccommodationAdapter accomAdapter;
    private ActivityUserAccomListCityBinding binding;
    RealmResults<Accommodation> accoms;
    List<Accommodation> accomList;
    List<Accommodation> filterAccoms;
    @Inject
    MyViewModelFactory viewModelFactory;
    private UserAccomListCityViewModel userAccCityVm;
    private City currentCity = null;
    private Observer<ErrorResponse> errorObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_accom_list_city);
        initInjec();
        getDataFromIntent();
        fetchData();
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
            userAccCityVm.fetchAccommodations(currentCity.getIdCity());
        } else {
            Toast.makeText(this, "No internet, data may be outdated", Toast.LENGTH_SHORT).show();
        }

    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        userAccCityVm = new ViewModelProvider(this, viewModelFactory).get(UserAccomListCityViewModel.class);
        binding.setViewModel(userAccCityVm);
        binding.setLifecycleOwner(this);
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 2));
        accoms = userAccCityVm.getAccomsByCityId(currentCity.getIdCity());
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
            accomList = userAccCityVm.realmResultToList(accoms);

            accomAdapter.submitList(accomList);
            accomAdapter.notifyDataSetChanged();
        });
        binding.rcvCity.setAdapter(accomAdapter);
    }
    private void addEvents(){
        final int[] minPrice = {0};
        final int[] maxPrice = {Integer.MAX_VALUE};
        final String[] query = {""};

        errorObserver = error -> {
            if (error != null) {
                showErrorDialog(UserAccomListCityActivity.this, error.getMessage());
                accomList = null;
                accomAdapter.submitList(accomList);
                accomAdapter.notifyDataSetChanged();

            }
        };
        userAccCityVm.getErrorLiveData().observeForever(errorObserver);

        binding.searchView.setQueryHint("Find by name or address");
        binding.btnBack.setOnClickListener(v -> {
            if (errorObserver != null) {
                userAccCityVm.getErrorLiveData().removeObserver(errorObserver);
            }
            userAccCityVm.clearErrorLiveData();
            Intent intent = new Intent(UserAccomListCityActivity.this, UserCityListActivity.class);
            startActivity(intent);
        });
        binding.btnFilter.setOnClickListener(v -> {
            if (binding.layFilter.getVisibility() == View.VISIBLE) {
                binding.layFilter.setVisibility(View.GONE);
            } else {
                binding.layFilter.setVisibility(View.VISIBLE);
            }
        });
        binding.rsbPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (accomList == null){
                    return;
                }
                minPrice[0] = 0;
                maxPrice[0] = (progress);
                String budgetText = "VND " + NumberFormat.getInstance().format(0) + " - VND " + NumberFormat.getInstance().format(maxPrice[0]);
                binding.txvRangePrice.setText(budgetText);
                filterAccoms = userAccCityVm.filterAccoms(accomList, query[0] ,minPrice[0], maxPrice[0]);
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
                if (accomList == null){
                    return false;
                }
                query[0] = newText;
                filterAccoms = userAccCityVm.filterAccoms(accomList, query[0] ,minPrice[0], maxPrice[0]);
                reLoad();
                return true;
            }
        });
        binding.calendarView.state().edit().setMinimumDate(CalendarDay.today()).commit();
        binding.btnConfirm.setOnClickListener(v -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                handleSearchByDay();
            } else {
                Toast.makeText(this, "No internet, can not search", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleSearchByDay(){
        for (Accommodation accommodation: accomList){
            userAccCityVm.addAccommodationFirestore(accommodation);
        }
        List<CalendarDay> selectedDates = binding.calendarView.getSelectedDates();
        if (selectedDates.size() >= 2) {
            userAccCityVm.isLoading.setValue(true);
            Date startDate = userAccCityVm.convertToDate(selectedDates.get(0));
            Date endDate = userAccCityVm.convertToDate(selectedDates.get(selectedDates.size() - 1));
            CallbackHelper onCompleteCallback = new CallbackHelper() {
                private int completedCount = 0;
                @Override
                public synchronized void onComplete() {
                    completedCount++;
                    if (completedCount == accomList.size()) {
                        accomAdapter.submitList(accomList);
                        userAccCityVm.isLoading.setValue(false);
                        binding.layFilter.setVisibility(View.GONE);
                        accomAdapter.notifyDataSetChanged();
                    }
                }
            };
            for (int i = 0; i < accomList.size(); i++){
                int finalI = i;
                userAccCityVm.getFreeroom(accomList.get(i).getAccommodationId(), startDate, endDate, new CallbackHelper() {
                    @Override
                    public void onDataReceived(int freeRoom) {
                        accomList.get(finalI).setCurrentFreeroom(freeRoom);
                        onCompleteCallback.onComplete();
                    }
                });
            }
        }else {
            Toast.makeText(UserAccomListCityActivity.this, "Please select a range of dates.", Toast.LENGTH_SHORT).show();
        }
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
        if (errorObserver != null) {
            userAccCityVm.getErrorLiveData().removeObserver(errorObserver);
        }
        userAccCityVm.clearErrorLiveData();
        accoms.removeAllChangeListeners();
    }
}