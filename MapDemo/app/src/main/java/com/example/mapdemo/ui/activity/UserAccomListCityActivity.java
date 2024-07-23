package com.example.mapdemo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mapdemo.R;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserAccomListCityBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.NumberFormat;

public class UserAccomListCityActivity extends BaseActivity<UserAccomListCityViewModel, ActivityUserAccomListCityBinding> {
    private AccommodationAdapter accomAdapter;
    private Observer<String> errorObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        getDataFromIntent();
        fetchData();
        setUpRecycleView();
        addEvents();
    }
    @Override
    protected Class<UserAccomListCityViewModel> getViewModelClass() {
        return UserAccomListCityViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_accom_list_city;
    }
    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }
    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idCity = i.getStringExtra("idCity");
        String name = i.getStringExtra("name");
        City currentCity = new City(idCity, name);
        viewModel.setCurrentCity(currentCity);
    }
    private void fetchData(){
        viewModel.fetchData(isNetworkAvailable(), new CallbackHelper() {
            @Override
            public void onNetworkError() {
                showToast("No internet, data may be outdated");
            }
        });
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rcvCity.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this,R.drawable.divider));
        binding.rcvCity.addItemDecoration(dividerItemDecoration);
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
                binding.txvInforAcc.setVisibility(View.VISIBLE);
                accomAdapter.submitList(null);
            }
        };
        viewModel.getErrorLiveData().observeForever(errorObserver);
        viewModel.getOnListChange().observe(this, onListChange->{
                    binding.srlReload.setRefreshing(false);
                    accomAdapter.submitList(viewModel.getAccomList());
                });
        binding.srlReload.setOnRefreshListener(this::fetchData);
        binding.btnBack.setOnClickListener(v -> {
            if (errorObserver != null) {
                viewModel.getErrorLiveData().removeObserver(errorObserver);
            }
            onBackPressed();
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
                viewModel.setPrice(budgetText);
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }
                filterRunnable = () -> {
                    viewModel.filterAccoms(query[0] ,minPrice[0], maxPrice[0]);
                    reLoad();
                };
                handler.postDelayed(filterRunnable, 200);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
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
                    viewModel.filterAccoms(query[0] ,minPrice[0], maxPrice[0]);
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
        viewModel.handleSearchByDay(binding.calendarView.getSelectedDates(), new CallbackHelper() {
            private int completedCount = 0;
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public synchronized void onComplete() {
                completedCount++;
                int size = viewModel.getAccomList().size();
                if (completedCount == size) {
                    accomAdapter.notifyDataSetChanged();
                    viewModel.isLoading.setValue(false);
                    binding.layFilter.setVisibility(View.GONE);
                }
            }
            @Override
            public void onDateError() {
                showToast("Please select a range of dates.");
            }
        });
    }
    private void reLoad(){
        if (viewModel.getAccomList().size() == 0){
            binding.txvInfor.setVisibility(View.VISIBLE);
        }else {
            binding.txvInfor.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (errorObserver != null) {
            viewModel.getErrorLiveData().removeObserver(errorObserver);
        }
        viewModel.getOnListChange().removeObservers(this);
    }
}