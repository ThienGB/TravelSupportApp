package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.DialogHelper.showErrorDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.BR;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserCityListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;

public class UserCityListActivity extends BaseActivity<UserCityListViewModel, ActivityUserCityListBinding> {
    private CityAdapter cityAdapter;
    private int countryCode;
    private Observer<String> errorObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        fetchData();
        setUpRecycleView();
        addEvents();
    }
    @Override
    protected Class<UserCityListViewModel> getViewModelClass() {
        return UserCityListViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_city_list;
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
        Intent intent = getIntent();
        countryCode = intent.getIntExtra("countryCode", 1);
    }
    private void fetchData(){
        viewModel.fetchData(isNetworkAvailable(), countryCode, new CallbackHelper() {
            @Override
            public void onNetworkError() {
                showToast("No internet, data may be outdated");
            }
        });
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 2));
        cityAdapter = new CityAdapter(city -> {
            Intent intent = new Intent(UserCityListActivity.this, UserAccomListCityActivity.class);
            intent.putExtra("idCity", city.getIdCity());
            intent.putExtra("name", city.getName());
            startActivity(intent);
        }, city -> {});
        binding.rcvCity.setAdapter(cityAdapter);
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            if (errorObserver != null) {
                viewModel.getErrorLiveData().removeObserver(errorObserver);
            }
            viewModel.clearErrorLiveData();
            Intent intent = new Intent(UserCityListActivity.this, UserSelectCountryActivity.class);
            startActivity(intent);
        });
        binding.srlReload.setOnRefreshListener(this::fetchData);
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
                    viewModel.filterCities(newText);
                    updateStatus();
                };
                handler.postDelayed(runnable, 400);
                return true;
            }
        });
        errorObserver = error -> {
            if (error != null) {
                showErrorDialog(this, error);
                binding.srlReload.setRefreshing(false);
            }
        };
        viewModel.getErrorLiveData().observeForever(errorObserver);
        viewModel.getOnListChange().observe(this, onListChange-> {
            cityAdapter.submitList(viewModel.getCitiList());
            binding.srlReload.setRefreshing(false);
        });
    }
    private void updateStatus(){
        if (viewModel.getCitiList().size() == 0){
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
            viewModel.getErrorLiveData().removeObserver(errorObserver);
        }
        viewModel.getOnListChange().removeObservers(this);
        viewModel.clearErrorLiveData();
    }
}