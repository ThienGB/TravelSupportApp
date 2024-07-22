package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.mapdemo.BR;
import com.example.mapdemo.R;
import com.example.mapdemo.databinding.ActivityUserBookingListBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.BookingAdapter;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.UserBookingListViewModel;

public class UserBookingListActivity extends BaseActivity<UserBookingListViewModel, ActivityUserBookingListBinding> {
    private BookingAdapter bookingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpRecycleView();
        addEvents();
    }
    @Override
    protected Class<UserBookingListViewModel> getViewModelClass() {
        return UserBookingListViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_booking_list;
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
        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 1));
        viewModel.loadBookingByIdUser();
        bookingAdapter = new BookingAdapter(viewModel);
        binding.rcvFavorite.setAdapter(bookingAdapter);
    }
    private void addEvents(){
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserBookingListActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();
        });
        viewModel.getOnListChange().observe(this, onChange -> {
            if (viewModel.getBookedList().size() == 0){
                String str = "There are no booked room yet";
                binding.txvInfor.setText(str);
            }
            bookingAdapter.submitList(viewModel.getBookedList());
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
        viewModel.getOnListChange().removeObservers(this);
    }
}