package com.example.mapdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.data.model.FirebaseBooking;
import com.example.mapdemo.databinding.ActivityAccomInforBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.AccomInforViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

public class AccomInforActivity extends AppCompatActivity {
    private ActivityAccomInforBinding binding;
    @Inject
    RealmHelper realmHelper;
    @Inject
    ViewModelFactory viewModelFactory;
    private AccomInforViewModel accInforVm;
    private Accommodation currentAccom = null;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accom_infor);
        getDataFromIntent();
        initInjec();
        loadData();
        addEvents();
    }

    private void getDataFromIntent(){
        Intent i = getIntent();
        String idAccom = i.getStringExtra("idAccom");
        String nameAccom = i.getStringExtra("nameAccom");
        firebaseAuth= FirebaseAuth.getInstance();
        if (idAccom != null) {
            currentAccom = new Accommodation(idAccom, nameAccom, 0);
        }
    }
    private void addEvents(){
        accInforVm.getIsFavorite().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavorite) {
                if (isFavorite != null) {
                    String idFavorite = firebaseAuth.getCurrentUser().getEmail() + currentAccom.getAccommodationId();
                    if (isFavorite) {
                        Favorite favorite = new Favorite(idFavorite,
                                currentAccom.getAccommodationId(),
                                firebaseAuth.getCurrentUser().getEmail(),
                                "accommodation");
                        accInforVm.addFavorite(favorite);
                    } else {
                        accInforVm.deleteFavorite(idFavorite);
                    }
                }
            }
        });
        binding.btnBooking.setOnClickListener(v -> showCalendarDialog());
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccomInforActivity.this, UserAccomListCityActivity.class);
                intent.putExtra("idCity", currentAccom.getCityId());
                intent.putExtra("nameAccom", currentAccom.getName());
                startActivity(intent);
            }
        });
    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        realmHelper.openRealm();
        accInforVm = new ViewModelProvider(this, viewModelFactory).get(AccomInforViewModel.class);
        binding.setViewModel(accInforVm);
        binding.setLifecycleOwner(this);
    }
    private void loadData(){
        boolean isFavorite = accInforVm.findFavoriteById(firebaseAuth.getCurrentUser().getEmail()+currentAccom.getAccommodationId());
        accInforVm.setFavorite(isFavorite);
        currentAccom = accInforVm.getAccommodation(currentAccom.getAccommodationId());
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

        btnConfirm.setOnClickListener(v -> {
            new AlertDialog.Builder(AccomInforActivity.this)
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
                                        handleBooking(selectedDates, startDate, endDate, numOfRooms);
                                        dialog.dismiss();
                                        startActivity(new Intent(AccomInforActivity.this,
                                                UserBookingListActivity.class));
                                    }else {
                                        Toast.makeText(AccomInforActivity.this, "There are no available rooms.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(AccomInforActivity.this, "Please select a range of dates.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                    .create()
                    .show();
        });

        dialog.show();
    }
    private void handleBooking(List<CalendarDay> selectedDates, Date startDate, Date endDate, int numOfRooms){
        String idBooking = UUID.randomUUID().toString();
        int price = currentAccom.getPrice() * accInforVm.getDaysBetween(selectedDates.get(0),
                selectedDates.get(selectedDates.size() - 1));
        String idUser = firebaseAuth.getCurrentUser().getEmail();
        FirebaseBooking firebaseBooking = new FirebaseBooking(idBooking,
                currentAccom.getAccommodationId(), idUser, startDate.getTime(),
                endDate.getTime(), price, numOfRooms);
        accInforVm.addFirebaseBooking(firebaseBooking);
        Booking booking = new Booking(idBooking, currentAccom.getAccommodationId(),
                idUser, startDate, endDate, price, numOfRooms);
        accInforVm.addBooking(booking);
        Toast.makeText(AccomInforActivity.this, "Booking Succesfully.", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.closeRealm();
    }
}