package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_FAVORITE_VIEW;
import static com.example.mapdemo.helper.ActionHelper.ACTION_RESEARCH_VIEW;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.mapdemo.R;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.databinding.ActivityAccomInforBinding;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.base.BaseActivity;
import com.example.mapdemo.ui.viewmodel.AccomInforViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AccomInforActivity extends BaseActivity {
    private ActivityAccomInforBinding binding;
    private AccomInforViewModel accInforVm;
    private AccommodationResponse currentAccom = null;
    private FirebaseAuth firebaseAuth;
    private int currentAction;
    private boolean isFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accom_infor);
        getDataFromIntent();
        accInforVm = getViewModel(AccomInforViewModel.class);
        binding.setViewModel(accInforVm);
        binding.setLifecycleOwner(this);
        loadRemoteData();
        addEvents();
    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idAccom = i.getStringExtra("idAccom");
        String nameAccom = i.getStringExtra("nameAccom");
        currentAction = i.getIntExtra("action", ACTION_RESEARCH_VIEW);
        firebaseAuth= FirebaseAuth.getInstance();
        if (idAccom != null) {
            currentAccom = new AccommodationResponse(idAccom, nameAccom);
        }
    }
    private void addEvents(){
        accInforVm.getIsFavorite().observe(this, isFavorite -> {
            if (isNetworkAvailable()) {
                if (!isFirstTime)
                    handleFavorite(isFavorite);
            } else {
                showToast("No internet");
            }
            isFirstTime = false;
        });
        binding.btnBooking.setOnClickListener(v -> showCalendarDialog());
        binding.btnBack.setOnClickListener(v -> {
            removeEvents();
            if (currentAction == ACTION_RESEARCH_VIEW){
                Intent intent = new Intent(AccomInforActivity.this, UserAccomListCityActivity.class);
                intent.putExtra("idCity", currentAccom.getCityId());
                intent.putExtra("nameAccom", currentAccom.getName());
                startActivity(intent);
            }else if (currentAction == ACTION_FAVORITE_VIEW) {
                Intent intent = new Intent(AccomInforActivity.this, UserFavoriteListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadRemoteData(){
        accInforVm.setIsLoading(false);
        loadFavoriteStatus();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            accInforVm.setIsLoading(true);
            accInforVm.getAccommodationFirestore(currentAccom.getAccommodationId(), new CallbackHelper() {
                @Override
                public void onAccommodationRecieved(Accommodation accommodation) {
                    loadLocalData();
                    accInforVm.setIsLoading(false);
                }
            });
        } else {
            loadLocalData();
            showToast("No internet, data may be outdated");
        }
    }
    private void loadFavoriteStatus(){
        boolean isFavorite = accInforVm.findFavoriteById(Objects.requireNonNull(
                firebaseAuth.getCurrentUser()).getEmail()+currentAccom.getAccommodationId());
        accInforVm.setFavorite(isFavorite);
    }
    private void loadLocalData(){
        currentAccom = accInforVm.getAccommodationRes(currentAccom.getAccommodationId());
        binding.txvAccomName.setText(currentAccom.getName());
        binding.txvFreeRoom.setText(String.valueOf(currentAccom.getFreeroom()));
        binding.txvDescription.setText(currentAccom.getDescription());
        binding.txvPrice.setText(String.valueOf(currentAccom.getPrice()));
        binding.txvAddress.setText(currentAccom.getAddress());
        Picasso.get().load(currentAccom.getImage()).into(binding.imgAccom);
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
                        Date startDate = accInforVm.convertToDate(selectedDates.get(0));
                        Date endDate = accInforVm.convertToDate(selectedDates.get(selectedDates.size() - 1));
                        int numOfRooms = numberPicker.getValue();
                        accInforVm.checkFreeRoom(currentAccom.getAccommodationId(), startDate, endDate, numOfRooms, new CallbackHelper() {
                            @Override
                            public void onRoomChecked(boolean isAvailable) {
                                if (isAvailable) {
                                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                                    if (networkInfo != null) {
                                        handleBooking(selectedDates, startDate, endDate, numOfRooms);
                                        startActivity(new Intent(AccomInforActivity.this,
                                                UserBookingListActivity.class));
                                    } else {
                                        showToast("No internet, can not booking");
                                    }
                                    dialog.dismiss();
                                }else {
                                    showToast("There are no available rooms.");
                                }
                            }
                        });
                    }else {
                        showToast("Please select a range of dates.");
                    }
                })
                .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                .create()
                .show());
        dialog.show();
    }
    private void handleBooking(List<CalendarDay> selectedDates, Date startDate, Date endDate, int numOfRooms){
        String idBooking = UUID.randomUUID().toString();
        int price = currentAccom.getPrice() * accInforVm.getDaysBetween(selectedDates.get(0),
                selectedDates.get(selectedDates.size() - 1)) * numOfRooms;
        String idUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        FirebaseBooking firebaseBooking = new FirebaseBooking(idBooking,
                currentAccom.getAccommodationId(), idUser, startDate.getTime(),
                endDate.getTime(), price, numOfRooms);
        accInforVm.addFirebaseBooking(firebaseBooking);
        Booking booking = new Booking(idBooking, currentAccom.getAccommodationId(),
                idUser, startDate, endDate, price, numOfRooms);
        accInforVm.addBooking(booking);
        showToast("Booking Succesfully.");
    }
    private void handleFavorite(Boolean isFavorite){
        String idFavorite = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()
                + currentAccom.getAccommodationId();
        if (isFavorite) {
            Favorite favorite = new Favorite(idFavorite,
                    currentAccom.getAccommodationId(),
                    firebaseAuth.getCurrentUser().getEmail(),
                    "accommodation");
            accInforVm.addFavorite(favorite);
            accInforVm.addFavoriteFirestore(favorite);
            showToast("Add "+ currentAccom.getName()+ " to favorite list successfully.");
        } else {
            accInforVm.deleteFavorite(idFavorite);
            accInforVm.deleteFavoriteFirestore(idFavorite);
            showToast("Remove "+ currentAccom.getName()+ " from favorite list successfully.");
        }
    }
    private void removeEvents(){
        accInforVm.getIsFavorite().removeObservers(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.btnBack.callOnClick();
    }
    @Override
    protected void onDestroy() {
        removeEvents();
        accInforVm.removeAllListeners();
        super.onDestroy();
    }
}