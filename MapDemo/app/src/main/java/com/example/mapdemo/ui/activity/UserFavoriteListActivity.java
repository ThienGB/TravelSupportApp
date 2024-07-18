package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_FAVORITE_VIEW;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.databinding.ActivityUserFavoriteListBinding;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.adapter.FavoriteAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserFavoriteListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import java.util.Objects;

public class UserFavoriteListActivity extends BaseActivity {
    private FavoriteAdapter favoriteAdapter;
    private ActivityUserFavoriteListBinding binding;
    private UserFavoriteListViewModel userFavoriteLstVm;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_favorite_list);
        userFavoriteLstVm = getViewModel(UserFavoriteListViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        setUpRecycleView();
        addEvents();
    }
    private void setUpRecycleView(){
        binding.srlReload.setRefreshing(true);
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        favoriteAdapter = new FavoriteAdapter(accommodation -> {
            Intent intent = new Intent(UserFavoriteListActivity.this, AccomInforActivity.class);
            intent.putExtra("idAccom", accommodation.getAccommodationId());
            intent.putExtra("nameAccom", accommodation.getName());
            intent.putExtra("action", ACTION_FAVORITE_VIEW);
            startActivity(intent);
            finish();
        }, (accommodation, isFavorite) -> {
            if (isNetworkAvailable()) {
                handleUnFavorite(accommodation, isFavorite);
            } else {
                showToast("No internet, can not unfavorite");
            }
        });
        loadRemoteData();
        binding.rcvFavorite.setAdapter(favoriteAdapter);
    }
    private void loadRemoteData(){
        if (isNetworkAvailable()) {
            userFavoriteLstVm.loadFavoriteAccomFirestore(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(),
                    new CallbackHelper() {
                        @Override
                        public void onListAccomRecieved(List<Accommodation> accommodations) {
                            loadLocalData();
                        }
                    });
        }else {
            loadLocalData();
            showToast("No internet, data may be outdated");
        }
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserFavoriteListActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();
        });
        binding.srlReload.setOnRefreshListener(this::loadRemoteData);
        userFavoriteLstVm.getOnListChange().observe(this, onChange -> {
            if (userFavoriteLstVm.getFavoriteAccomList().size() == 0){
                String str = "There are no favorites yet";
                binding.txvInfor.setText(str);
            }
            favoriteAdapter.submitList(userFavoriteLstVm.getFavoriteAccomList());
        });
    }
    private void handleUnFavorite(Accommodation accommodation, boolean isFavorite){
        String idFavorite = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()
                + accommodation.getAccommodationId();
        if (isFavorite) {
            Favorite favorite = new Favorite(idFavorite,
                    accommodation.getAccommodationId(),
                    firebaseAuth.getCurrentUser().getEmail(),
                    "accommodation");
            userFavoriteLstVm.addFavorite(favorite);
            userFavoriteLstVm.addFavoriteFirestore(favorite);
        } else {
            userFavoriteLstVm.deleteFavorite(idFavorite);
            userFavoriteLstVm.deleteFavoriteFirestore(idFavorite);
        }
    }
    private void loadLocalData(){
        new Handler(Looper.getMainLooper()).post(() -> {
            userFavoriteLstVm.loadFavoriteAccomList(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
            binding.srlReload.setRefreshing(false);
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.btnBack.callOnClick();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}