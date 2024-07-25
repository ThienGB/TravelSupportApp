package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_FAVORITE_VIEW;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserFavoriteListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.FavoriteAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserFavoriteListViewModel;

public class UserFavoriteListActivity extends BaseActivity<UserFavoriteListViewModel, ActivityUserFavoriteListBinding> {
    private FavoriteAdapter favoriteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpRecycleView();
        addEvents();
    }
    @Override
    protected Class<UserFavoriteListViewModel> getViewModelClass() {
        return UserFavoriteListViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_favorite_list;
    }
    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }
    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
    private void setUpRecycleView(){
        binding.srlReload.setRefreshing(true);
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rcvFavorite.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this,R.drawable.divider));
        binding.rcvFavorite.addItemDecoration(dividerItemDecoration);
        favoriteAdapter = new FavoriteAdapter(accommodation -> {
            Intent intent = new Intent(UserFavoriteListActivity.this, AccomInforActivity.class);
            intent.putExtra("idAccom", accommodation.getAccommodationId());
            intent.putExtra("nameAccom", accommodation.getName());
            intent.putExtra("action", ACTION_FAVORITE_VIEW);
            startActivity(intent);
        }, (accommodation, isFavorite) -> viewModel.handleFavorite(accommodation, isFavorite, isNetworkAvailable(), new CallbackHelper() {
            @Override
            public void onNetworkError() {
                showToast("No internet, can not unfavorite");
            }
        }));
        loadData();
        binding.rcvFavorite.setAdapter(favoriteAdapter);
    }
    private void loadData(){
        viewModel.loadData(isNetworkAvailable(), new CallbackHelper() {
            @Override
            public void onNetworkError() {
                showToast("No internet, data may be outdated");
                binding.srlReload.setRefreshing(false);
                binding.txvInfor.setVisibility(View.GONE);
            }
            @Override
            public void onComplete() {
                binding.srlReload.setRefreshing(false);
                binding.txvInfor.setVisibility(View.GONE);
            }
            @Override
            public void onListEmpty(){
                binding.srlReload.setRefreshing(false);
                binding.txvInfor.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAccommodationDeleted(){
                loadData();
            }
        });
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserFavoriteListActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();
        });
        binding.srlReload.setOnRefreshListener(this::loadData);
        viewModel.getOnListChange().observe(this, onChange -> favoriteAdapter.submitList(viewModel.getFavoriteAccomList()));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.btnBack.callOnClick();
    }
    @Override
    protected void onDestroy() {
        viewModel.getOnListChange().removeObservers(this);
        super.onDestroy();
    }
}