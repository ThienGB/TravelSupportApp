package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_FAVORITE_VIEW;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.databinding.ActivityUserFavoriteListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.adapter.FavoriteAdapter;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;
import com.example.mapdemo.ui.viewmodel.UserFavoriteListViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class UserFavoriteListActivity extends AppCompatActivity {
    private FavoriteAdapter favoriteAdapter;
    private ActivityUserFavoriteListBinding binding;
    @Inject
    MyViewModelFactory viewModelFactory;
    private UserFavoriteListViewModel userFavoriteListViewModel;
    private List<Accommodation> favoriteList;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_favorite_list);
        initInjec();
        setUpRecycleView();
        addEvents();
    }

    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        userFavoriteListViewModel = new ViewModelProvider(this, viewModelFactory).get(UserFavoriteListViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void setUpRecycleView(){
        binding.srlReload.setRefreshing(true);
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        favoriteAdapter = new FavoriteAdapter(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                Intent intent = new Intent(UserFavoriteListActivity.this, AccomInforActivity.class);
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameAccom", accommodation.getName());
                intent.putExtra("action", ACTION_FAVORITE_VIEW);
                startActivity(intent);
                finish();
            }
        }, new FavoriteAdapter.OnFavoriteListener() {

            @Override
            public void onFavoriteClick(Accommodation accommodation, boolean isFavorite) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    handleUnFavorite(accommodation, isFavorite);
                } else {
                    Toast.makeText(UserFavoriteListActivity.this, "No internet, can not unfavorite", Toast.LENGTH_SHORT).show();
                }

            }
        });
        loadRemoteData();
        binding.rcvFavorite.setAdapter(favoriteAdapter);
    }
    private void loadRemoteData(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            userFavoriteListViewModel.loadFavoriteAccomFirestore(firebaseAuth.getCurrentUser().getEmail(),
                    new CallbackHelper() {
                        @Override
                        public void onListAccomRecieved(List<Accommodation> accommodations) {
                            loadLocalData();
                        }
                    });
        }else {
            loadLocalData();
            Toast.makeText(this, "No internet, data may be outdated", Toast.LENGTH_SHORT).show();
        }
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserFavoriteListActivity.this, UserHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.srlReload.setOnRefreshListener(this::loadRemoteData);
    }
    private void handleUnFavorite(Accommodation accommodation, boolean isFavorite){
        String idFavorite = firebaseAuth.getCurrentUser().getEmail() + accommodation.getAccommodationId();
        if (isFavorite) {
            Favorite favorite = new Favorite(idFavorite,
                    accommodation.getAccommodationId(),
                    firebaseAuth.getCurrentUser().getEmail(),
                    "accommodation");
            userFavoriteListViewModel.addFavorite(favorite);
            userFavoriteListViewModel.addFavoriteFirestore(favorite);
        } else {
            userFavoriteListViewModel.deleteFavorite(idFavorite);
            userFavoriteListViewModel.deleteFavoriteFirestore(idFavorite);
        }
    }
    private void loadLocalData(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                userFavoriteListViewModel.loadFavoriteAccom(firebaseAuth.getCurrentUser().getEmail());
                userFavoriteListViewModel.favoriteAccom.removeAllChangeListeners();
                userFavoriteListViewModel.favoriteAccom.addChangeListener(new RealmChangeListener<RealmResults<Accommodation>>() {
                    @Override
                    public void onChange(RealmResults<Accommodation> accommodations) {
                        favoriteList = userFavoriteListViewModel.realmToList(accommodations);
                        favoriteAdapter.submitList(favoriteList);
                        favoriteAdapter.notifyDataSetChanged();
                    }
                });
                binding.srlReload.setRefreshing(false);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}