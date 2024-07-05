package com.example.mapdemo.ui.activity;

import android.content.DialogInterface;
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
import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.Favorite;
import com.example.mapdemo.databinding.ActivityAccomInforBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.viewmodel.AccomInforViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AccomInforActivity extends AppCompatActivity {
    private ActivityAccomInforBinding binding;
    @Inject
    RealmHelper realmHelper;
    @Inject
    ViewModelFactory viewModelFactory;
    private AccomInforViewModel accomInforViewModel;
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
        accomInforViewModel.getIsFavorite().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavorite) {
                if (isFavorite != null) {
                    String idFavorite = firebaseAuth.getCurrentUser().getEmail() + currentAccom.getAccommodationId();
                    if (isFavorite) {

                        Favorite favorite = new Favorite(idFavorite,
                                currentAccom.getAccommodationId(),
                                firebaseAuth.getCurrentUser().getEmail(),
                                "accommodation");
                        accomInforViewModel.addFavorite(favorite);
                    } else {
                        accomInforViewModel.deleteFavorite(idFavorite);
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
        accomInforViewModel = new ViewModelProvider(this, viewModelFactory).get(AccomInforViewModel.class);
        binding.setViewModel(accomInforViewModel);
        binding.setLifecycleOwner(this);
    }
    private void loadData(){
        boolean isFavorite = accomInforViewModel.findFavoriteById(firebaseAuth.getCurrentUser().getEmail()+currentAccom.getAccommodationId());
        accomInforViewModel.setFavorite(isFavorite);
        currentAccom = accomInforViewModel.getAccommodation(currentAccom.getAccommodationId());
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
        final Button btnConfirm = dialogView.findViewById(R.id.btnOk);
        AlertDialog dialog = builder.create();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AccomInforActivity.this);
                builder.setTitle("Confirm Booking");
                builder.setMessage("Are you sure you want to book a room?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        List<CalendarDay> selectedDates = calendarView.getSelectedDates();
                        if (selectedDates.size() >= 2) {
                            CalendarDay startDate = selectedDates.get(0);
                            CalendarDay endDate = selectedDates.get(selectedDates.size() - 1);
                            Date startDateAsDate = convertCalendarDayToDate(startDate);

                            Date endDateAsDate = convertCalendarDayToDate(endDate);
                            String idBooking = UUID.randomUUID().toString();
                            int price = currentAccom.getPrice() * getDaysBetween(startDate, endDate);
                            Booking booking = new Booking(idBooking,
                                    currentAccom.getAccommodationId(),
                                    firebaseAuth.getCurrentUser().getEmail(),
                                    startDateAsDate, endDateAsDate, price);
                            accomInforViewModel.addBooking(booking);
                            Toast.makeText(AccomInforActivity.this, "Booking Succesfully.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(AccomInforActivity.this, "Please select a range of dates.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });

        dialog.show();
    }
    private Date convertCalendarDayToDate(CalendarDay calendarDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarDay.getYear(), calendarDay.getMonth() - 1, calendarDay.getDay()); // Chú ý tháng trong Calendar bắt đầu từ 0
        return calendar.getTime();
    }
    public static int getDaysBetween(CalendarDay startDate, CalendarDay endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(startDate.getYear(), startDate.getMonth() - 1, startDate.getDay()); // Tháng bắt đầu từ 0

        Calendar endCal = Calendar.getInstance();
        endCal.set(endDate.getYear(), endDate.getMonth() - 1, endDate.getDay());

        long startMillis = startCal.getTimeInMillis();
        long endMillis = endCal.getTimeInMillis();
        long diffMillis = endMillis - startMillis;

        return (int) TimeUnit.MILLISECONDS.toDays(diffMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.closeRealm();
    }
}