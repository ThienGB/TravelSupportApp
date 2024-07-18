package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserBookingListBinding;
import com.example.mapdemo.ui.adapter.BookingAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserBookingListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class UserBookingListActivity extends BaseActivity {
    private ActivityUserBookingListBinding binding;
    private UserBookingListViewModel userBookingLstVm;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_booking_list);
        userBookingLstVm = getViewModel(UserBookingListViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        setUpRecycleView();
        addEvents();
    }
    private void setUpRecycleView(){
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 1));
        userBookingLstVm.loadBookingByIdUser(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
        BookingAdapter bookingAdapter = new BookingAdapter(new BookingAdapter.OnItemClickListener() {
        }, new BookingAdapter.OnItemLongClickListener() {
        }, userBookingLstVm);
        binding.rcvFavorite.setAdapter(bookingAdapter);
        userBookingLstVm.getOnListChange().observe(this, onChange -> {
            if (userBookingLstVm.getBookedList().size() == 0){
                String str = "There are no favorites yet";
                binding.txvInfor.setText(str);
            }
            bookingAdapter.submitList(userBookingLstVm.getBookedList());
        });
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserBookingListActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();
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
        userBookingLstVm.getOnListChange().removeObservers(this);
    }

}