package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.data.ActionHelper.ACTION_ADD_ACCOM;

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
import com.example.mapdemo.data.CopyRealmFile;
import com.example.mapdemo.data.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.databinding.ActivityAdminAccomListBinding;
import com.example.mapdemo.databinding.BottomSheetAccomBinding;
import com.example.mapdemo.databinding.DialogEditAccomBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.AccommodationAdapter;
import com.example.mapdemo.ui.viewmodel.AdminListAccomCityViewModel;
import com.example.mapdemo.ui.viewmodel.ViewModelFactory;

import java.util.UUID;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AdminAccomListActivity extends AppCompatActivity {
    private AccommodationAdapter accomAdapter;
    private ActivityAdminAccomListBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<Accommodation> accoms;
    @Inject
    ViewModelFactory viewModelFactory;
    private AdminListAccomCityViewModel adminAccomViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_accom_list);
        CopyRealmFile.copyRealmFile(this);
        initInjec();
        setUpRecycleView();
        addEvents();
        //addDumbData();
    }
    private void addDumbData(){
        Accommodation acc = new Accommodation("925dbc02-ab00-41ea-bbaf-f03511a53275", "Nhà nghỉ", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("c55020ec-5757-4a1e-bca2-e4c7917a0546", "Nhà nghỉ Hòa Bình 5 ⭐️", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("559cd6fa-ec0a-4f1f-b580-05839ea77086", "Homestay Vũng Tàu Beach", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("89e9d43b-1d3c-4cd6-a6cb-0c34f86583cd", "Penhouse Quảng Điền", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("0e59d722-36cb-46fb-a540-9f29d5bd7d5d", "Khách sạn Trường Tình", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("ea669a6a-8c35-4bdc-888d-5db2a5b29063", "Penhouse trên Đà Lạt", 0);
        adminAccomViewModel.addAccoms(acc);
        acc = new Accommodation("abfe9f93-5160-4161-b9ed-9559db151bd1", "Khách sạn ngàn sao", 0);
        adminAccomViewModel.addAccoms(acc);

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
        accoms = adminAccomViewModel.getAccoms();
        accomAdapter = new AccommodationAdapter(new AccommodationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                    Intent intent = new Intent(AdminAccomListActivity.this, AccomInforActivity.class);
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
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminAccomListActivity.this);
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
                        if (ten.equals("")){
                            Toast.makeText(AdminAccomListActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int gia = 0;
                        if (!giastr.equals("")){
                            gia = Integer.parseInt(giastr);
                        }
                        String id = UUID.randomUUID().toString();
                        Accommodation accommodation = new Accommodation(id, ten, gia);
                        adminAccomViewModel.addAccoms(accommodation);
                        Toast.makeText(AdminAccomListActivity.this, "Thêm thành nơi ở thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAccomListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showBottomSheet(Accommodation accommodation){
        final Dialog dialog=new Dialog(AdminAccomListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BottomSheetAccomBinding accomBinding= BottomSheetAccomBinding.inflate(getLayoutInflater());
        dialog.setContentView(accomBinding.getRoot());
        accomBinding.txvAccomName.setText(accommodation.getName());
        accomBinding.tbrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminAccomListActivity.this);
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
                        if (name.equals("")){
                            Toast.makeText(AdminAccomListActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int price = 0;
                        if (!priceStr.equals("")){
                            price = Integer.parseInt(priceStr);
                        }
                        Accommodation acc = new Accommodation(accommodation.getAccommodationId(), name, price);
                        adminAccomViewModel.updateAccoms(acc);
                        Toast.makeText(AdminAccomListActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminAccomListActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        adminAccomViewModel.deleteAccoms(accommodation.getAccommodationId());
                        Toast.makeText(AdminAccomListActivity.this, "Xóa thành phố thành công", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AdminAccomListActivity.this, AdminCityListActivity.class);
                intent.putExtra("idCity", accommodation.getCityId());
                intent.putExtra("idAccom", accommodation.getAccommodationId());
                intent.putExtra("nameAccom", accommodation.getName());
                intent.putExtra("action", ACTION_ADD_ACCOM);
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