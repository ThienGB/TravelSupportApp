package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_FAVORITE_VIEW;
import static com.example.mapdemo.helper.ActionHelper.ACTION_RESEARCH_VIEW;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.library.baseAdapters.BR;
import com.example.mapdemo.R;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.databinding.ActivityAccomInforBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.AccomInforViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.shawnlin.numberpicker.NumberPicker;
import java.util.List;

public class AccomInforActivity extends BaseActivity<AccomInforViewModel, ActivityAccomInforBinding> {
    private int currentAction;
    private boolean isFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        loadData();
        addEvents();
    }
    @Override
    protected Class<AccomInforViewModel> getViewModelClass() {
        return AccomInforViewModel.class;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_accom_infor;
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
        String idAccom = i.getStringExtra("idAccom");
        String nameAccom = i.getStringExtra("nameAccom");
        currentAction = i.getIntExtra("action", ACTION_RESEARCH_VIEW);
        if (idAccom != null) {
            viewModel.currentAccom = new AccommodationResponse(idAccom, nameAccom);
        }
    }
    private void addEvents(){
        viewModel.getIsFavorite().observe(this, isFavorite -> {
            if (isNetworkAvailable()) {
                if (!isFirstTime)
                    viewModel.handleFavorite(isFavorite, new CallbackHelper() {
                        @Override
                        public void onFavorite(String message) {
                            showToast(message);
                        }
                    });
            } else {
                showToast("No internet");
            }
            isFirstTime = false;
        });
        binding.btnBooking.setOnClickListener(v -> showCalendarDialog());
        binding.btnBack.setOnClickListener(v -> {
            removeEvents();
            if (currentAction == ACTION_RESEARCH_VIEW){
                onBackPressed();
            }else if (currentAction == ACTION_FAVORITE_VIEW) {
                Intent intent = new Intent(AccomInforActivity.this, UserFavoriteListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadData(){
        viewModel.loadInforAccom(isNetworkAvailable(), new CallbackHelper() {
            @Override
            public void onNetworkError() {
                showToast("No internet, data may be outdated");
            }
            @Override
            public void onAccommodationDeleted(){
                showToast("This accommodation does not exist");
                onBackPressed();
            }
        });
    }
    private void showCalendarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_booking, null);
        builder.setView(dialogView);
        final MaterialCalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        calendarView.state().edit().setMinimumDate(CalendarDay.today()).commit();
        NumberPicker numberPicker = dialogView.findViewById(R.id.npkNumOfRoom);
        final Button btnConfirm = dialogView.findViewById(R.id.btnOk);
        AlertDialog dialog = builder.create();
        btnConfirm.setOnClickListener(v -> new AlertDialog.Builder(AccomInforActivity.this)
                .setTitle("Confirm Booking")
                .setMessage("Are you sure you want to book a room?")
                .setPositiveButton("Yes", (dialogmini, which) -> {
                    List<CalendarDay> selectedDates = calendarView.getSelectedDates();
                    if (selectedDates.size() >= 2) {
                        if (isNetworkAvailable()){
                            int numOfRooms = numberPicker.getValue();
                            viewModel.handleBooking(selectedDates, numOfRooms, new CallbackHelper() {
                                @Override
                                public void onComplete() {
                                    Intent intent =new Intent(AccomInforActivity.this, UserBookingListActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onOutOfRoom() {
                                    showToast("There are no available rooms.");
                                }
                            });
                        }
                        else{
                            showToast("No internet, can not booking");
                            dialog.dismiss();
                        }
                    } else {showToast("Please select a range of dates.");}
                }).setNegativeButton("No", (dialog1, which) -> dialog1.dismiss()).create().show());
        dialog.show();
    }

    private void removeEvents(){
        viewModel.getIsFavorite().removeObservers(this);
    }
    @Override
    protected void onDestroy() {
        removeEvents();
        super.onDestroy();
    }
}