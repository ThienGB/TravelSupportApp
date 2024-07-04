package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.data.ActionHelper.ACTION_MOVE_ACCOM;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityAdminAccomListCityBinding;
import com.example.mapdemo.databinding.BottomSheetAccomCityBinding;
import com.example.mapdemo.databinding.DialogEditAccomBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.viewmodel.AdminListAccomCityViewModel;

import java.util.UUID;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AdminAccomListCityActivity extends AppCompatActivity {
    private AccommodationAdapter accomAdapter;
    private ActivityAdminAccomListCityBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<Accommodation> accoms;
    @Inject
    ViewModelFactory viewModelFactory;
    private AdminListAccomCityViewModel adminAccomViewModel;
    private City currentCity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_accom_list_city);

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
    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        activityComponent.inject(this);
        realmHelper.openRealm();
        adminAccomViewModel = new ViewModelProvider(this, viewModelFactory).get(AdminListAccomCityViewModel.class);
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 3));
        accoms = adminAccomViewModel.getAccomsByCity(currentCity.getIdCity());
        accomAdapter = new AccommodationAdapter(new AccommodationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                Intent intent = new Intent(AdminAccomListCityActivity.this, AccomInforActivity.class);
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameAccom", accommodation.getName());
                startActivity(intent);
            }

        }, new AccommodationAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Accommodation accommodation) {
                showBottomSheet(accommodation);
            }
        });
        binding.rcvCity.setAdapter(accomAdapter);
        accomAdapter.submitList(accoms);
        accomAdapter.notifyDataSetChanged();
    }
    private void addEvents(){
        accoms.addChangeListener(new RealmChangeListener<RealmResults<Accommodation>>() {
            @Override
            public void onChange(RealmResults<Accommodation> phongBans) {
                accomAdapter.submitList(accoms);
                accomAdapter.notifyDataSetChanged();
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListCityActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminAccomListCityActivity.this);
                builder.setTitle("Thêm chỗ ở");
                final View alertView = layoutInflater.inflate(R.layout.dialog_edit_accom, null);
                builder.setView(alertView);
                DialogEditAccomBinding editAccomBinding = DialogEditAccomBinding.inflate(getLayoutInflater());
                builder.setView(editAccomBinding.getRoot());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String ten = editAccomBinding.edtNameAccom.getText().toString();
                        String giastr = editAccomBinding.edtPrice.getText().toString();
                        String idCity = currentCity.getIdCity();
                        if (ten.equals("")){
                            Toast.makeText(AdminAccomListCityActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int gia = 0;
                        if (!giastr.equals("")){
                            gia = Integer.parseInt(giastr);
                        }
                        String id = UUID.randomUUID().toString();
                        Accommodation accommodation = new Accommodation(id, ten, gia, idCity);
                        adminAccomViewModel.addAccoms(accommodation);
                        Toast.makeText(AdminAccomListCityActivity.this, "Thêm thành nơi ở thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAccomListCityActivity.this, AdminCityListActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showBottomSheet(Accommodation accommodation){
        final Dialog dialog=new Dialog(AdminAccomListCityActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BottomSheetAccomCityBinding accomBinding= BottomSheetAccomCityBinding.inflate(getLayoutInflater());
        dialog.setContentView(accomBinding.getRoot());

        accomBinding.txvAccomName.setText(accommodation.getName());
        accomBinding.tbrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListCityActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminAccomListCityActivity.this);
                builder.setTitle("Chỉnh sửa nơi ở");
                final View alertView = layoutInflater.inflate(R.layout.dialog_edit_city, null);
                builder.setView(alertView);
                DialogEditAccomBinding editAccomBinding = DialogEditAccomBinding.inflate(getLayoutInflater());
                builder.setView(editAccomBinding.getRoot());
                editAccomBinding.edtNameAccom.setText(accommodation.getName());
                editAccomBinding.edtPrice.setText(String.valueOf(accommodation.getPrice()));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String name = editAccomBinding.edtNameAccom.getText().toString();
                        String priceStr = editAccomBinding.edtPrice.getText().toString();
                        String idCity = currentCity.getIdCity();
                        if (name.equals("")){
                            Toast.makeText(AdminAccomListCityActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int price = 0;
                        if (!priceStr.equals("")){
                            price = Integer.parseInt(priceStr);
                        }
                        Accommodation acc = new Accommodation(accommodation.getAccommodationId(), name,price, idCity);
                        adminAccomViewModel.updateAccoms(acc);
                        Toast.makeText(AdminAccomListCityActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        accomBinding.tbrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListCityActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        adminAccomViewModel.deleteAccoms(accommodation.getAccommodationId());
                        Toast.makeText(AdminAccomListCityActivity.this, "Xóa thành phố thành công", Toast.LENGTH_SHORT).show();
                        dialogmini.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });

        accomBinding.tbrMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAccomListCityActivity.this, AdminCityListActivity.class);
                intent.putExtra("idCity", currentCity.getIdCity());
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameCity", currentCity.getName());
                intent.putExtra("nameAccom", accommodation.getName());
                intent.putExtra("action", ACTION_MOVE_ACCOM);
                startActivity(intent);
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (accoms != null) {
            accoms.removeAllChangeListeners();
        }
        realmHelper.closeRealm();
    }
}