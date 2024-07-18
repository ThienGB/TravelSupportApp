package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserAccomListCityBinding;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class UserAccomListCityActivity extends BaseActivity {
    private AccommodationAdapter accomAdapter;
    private ActivityUserAccomListCityBinding binding;
    private UserAccomListCityViewModel userAccCityVm;
    private Observer<String> errorObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_accom_list_city);
        userAccCityVm = getViewModel(UserAccomListCityViewModel.class);
        binding.setViewModel(userAccCityVm);
        binding.setLifecycleOwner(this);
        getDataFromIntent();
        fetchData();
        setUpRecycleView();
        addEvents();
    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idCity = i.getStringExtra("idCity");
        String name = i.getStringExtra("name");
        City currentCity = new City(idCity, name);
        userAccCityVm.setCurrentCity(currentCity);
    }
    private void fetchData(){
        userAccCityVm.clearErrorLiveData();
        if (isNetworkAvailable()) {
            userAccCityVm.fetchAccommodations();
        } else {
            userAccCityVm.loadAccomList();
            showToast("No internet, data may be outdated");
        }
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 2));
        accomAdapter = new AccommodationAdapter(accommodation -> {
            Intent intent = new Intent(UserAccomListCityActivity.this, AccomInforActivity.class);
            intent.putExtra("idAccom", accommodation.getAccommodationId());
            intent.putExtra("nameAccom", accommodation.getName());
            startActivity(intent);
        }, accommodation -> {});
        binding.rcvCity.setAdapter(accomAdapter);
    }
    private void addEvents(){
        final int[] minPrice = {0};
        final int[] maxPrice = {Integer.MAX_VALUE};
        final String[] query = {""};
        errorObserver = error -> {
            if (error != null) {
                showErrorDialog(UserAccomListCityActivity.this, error);
                binding.srlReload.setRefreshing(false);
                accomAdapter.submitList(null);
            }
        };
        binding.srlReload.setOnRefreshListener(this::fetchData);
        userAccCityVm.getErrorLiveData().observeForever(errorObserver);
        userAccCityVm.getOnListChange().observe(this, onListChange->{
                    binding.srlReload.setRefreshing(false);
                    accomAdapter.submitList(userAccCityVm.getAccomList());
                });
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
            private final Handler handler = new Handler();
            private Runnable filterRunnable;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrice[0] = (progress);
                String budgetText = "VND " + NumberFormat.getInstance().format(0) + " - VND " + NumberFormat.getInstance().format(maxPrice[0]);
                binding.txvRangePrice.setText(budgetText);
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }
                filterRunnable = () -> {
                    userAccCityVm.filterAccoms(query[0] ,minPrice[0], maxPrice[0]);
                    reLoad();
                };
                handler.postDelayed(filterRunnable, 200);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
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
                    query[0] = newText;
                    userAccCityVm.filterAccoms(query[0] ,minPrice[0], maxPrice[0]);
                    reLoad();
                };
                handler.postDelayed(runnable, 400);
                return true;
            }
        });
        binding.calendarView.state().edit().setMinimumDate(CalendarDay.today()).commit();
        binding.btnConfirm.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                handleSearchByDay();
            } else {
                showToast("No internet, can not search");
            }
        });
    }
    private void handleSearchByDay(){
        for (Accommodation accommodation: userAccCityVm.getAccomList()){
            userAccCityVm.addAccommodationFirestore(accommodation);
        }
        List<CalendarDay> selectedDates = binding.calendarView.getSelectedDates();
        if (selectedDates.size() >= 2) {
            userAccCityVm.isLoading.setValue(true);
            Date startDate = userAccCityVm.convertToDate(selectedDates.get(0));
            Date endDate = userAccCityVm.convertToDate(selectedDates.get(selectedDates.size() - 1));
            CallbackHelper onCompleteCallback = new CallbackHelper() {
                private int completedCount = 0;
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public synchronized void onComplete() {
                    completedCount++;
                    int size = userAccCityVm.getAccomList().size();
                    if (completedCount == size) {
                        accomAdapter.notifyDataSetChanged();
                        userAccCityVm.isLoading.setValue(false);
                        binding.layFilter.setVisibility(View.GONE);
                    }
                }
            };
            for (int i = 0; i < userAccCityVm.getAccomList().size(); i++){
                int finalI = i;
                userAccCityVm.getFreeroom(userAccCityVm.getAccomList().get(finalI).getAccommodationId(), startDate, endDate, new CallbackHelper() {
                    @Override
                    public void onDataReceived(int freeRoom) {
                        userAccCityVm.setCurrentFreeRoom(finalI, freeRoom);
                        onCompleteCallback.onComplete();
                    }
                });
            }
        }else {
            showToast("Please select a range of dates.");
        }
    }
    private void reLoad(){
        if (userAccCityVm.getAccomList().size() == 0){
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
            userAccCityVm.getErrorLiveData().removeObserver(errorObserver);
        }
        userAccCityVm.clearErrorLiveData();
        userAccCityVm.getOnListChange().removeObservers(this);
    }
}