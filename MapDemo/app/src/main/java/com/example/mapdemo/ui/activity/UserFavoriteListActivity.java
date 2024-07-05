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
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserAccomListCityBinding;
import com.example.mapdemo.databinding.ActivityUserCityListBinding;
import com.example.mapdemo.databinding.ActivityUserFavoriteListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.viewmodel.UserAccomListCityViewModel;
import com.example.mapdemo.ui.viewmodel.UserCityListViewModel;
import com.example.mapdemo.ui.viewmodel.UserFavoriteListViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;

public class UserFavoriteListActivity extends AppCompatActivity {
    private AccommodationAdapter accomAdapter;
    private ActivityUserFavoriteListBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmList<Accommodation> accoms;
    @Inject
    ViewModelFactory viewModelFactory;
    private UserFavoriteListViewModel userFavoriteListViewModel;
    private City currentCity = null;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_favorite_list);
        initInjec();
        getDataFromIntent();
        setUpRecycleView();
        addEvents();
    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idCity = i.getStringExtra("idCity");
        String name = i.getStringExtra("name");
        currentCity = new City(idCity, name);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        realmHelper.openRealm();
        userFavoriteListViewModel = new ViewModelProvider(this, viewModelFactory).get(UserFavoriteListViewModel.class);
    }
    private void setUpRecycleView(){
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        accoms = userFavoriteListViewModel.getFavoriteByIdUser(firebaseAuth.getCurrentUser().getEmail());
        if (accoms == null){
            binding.txvInfor.setText("There are no favorites yet");
            return;
        }

        accomAdapter = new AccommodationAdapter(new AccommodationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                Intent intent = new Intent(UserFavoriteListActivity.this, AccomInforActivity.class);
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameAccom", accommodation.getName());
                startActivity(intent);
            }

        }, new AccommodationAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Accommodation accommodation) {
            }
        });
        binding.rcvFavorite.setAdapter(accomAdapter);
        accomAdapter.submitList(accoms);
        accomAdapter.notifyDataSetChanged();
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserFavoriteListActivity.this, UserHomeActivity.class);
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