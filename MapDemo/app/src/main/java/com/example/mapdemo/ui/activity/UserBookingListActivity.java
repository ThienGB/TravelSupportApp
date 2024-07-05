package com.example.mapdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityUserBookingListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.adapter.BookingAdapter;
import com.example.mapdemo.ui.viewmodel.UserBookingListViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.realm.RealmResults;

public class UserBookingListActivity extends AppCompatActivity {
    private BookingAdapter bookingAdapter;
    private ActivityUserBookingListBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<Booking> bookings;
    @Inject
    ViewModelFactory viewModelFactory;
    private UserBookingListViewModel userBookingListViewModel;
    private City currentCity = null;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_booking_list);
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
        userBookingListViewModel = new ViewModelProvider(this, viewModelFactory).get(UserBookingListViewModel.class);
    }
    private void setUpRecycleView(){
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 1));
        bookings = userBookingListViewModel.getBookingByIdUser(firebaseAuth.getCurrentUser().getEmail());
        if (bookings == null){
            binding.txvInfor.setText("There are no favorites yet");
            return;
        }
        bookingAdapter = new BookingAdapter(new BookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Booking booking) {
            }
        }, new BookingAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Booking booking) {
            }
        }, userBookingListViewModel);
        binding.rcvFavorite.setAdapter(bookingAdapter);
        bookingAdapter.submitList(bookings);
        bookingAdapter.notifyDataSetChanged();
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserBookingListActivity.this, UserHomeActivity.class);
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