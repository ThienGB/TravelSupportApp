package com.example.mapdemo.ui.activity;

import static com.example.mapdemo.helper.ActionHelper.ACTION_ADD_ACCOM;
import static com.example.mapdemo.helper.ActionHelper.ACTION_EDIT;
import static com.example.mapdemo.helper.ActionHelper.ACTION_MOVE_ACCOM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

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

import com.example.mapdemo.MainApplication;
import com.example.mapdemo.R;
import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.ActivityAdminCityListBinding;
import com.example.mapdemo.databinding.BottomSheetCityBinding;
import com.example.mapdemo.databinding.DialogEditCityBinding;
import com.example.mapdemo.di.component.ActivityComponent;
import com.example.mapdemo.ui.adapter.CityAdapter;
import com.example.mapdemo.ui.viewmodel.AdminCityListViewModel;
import com.example.mapdemo.ui.viewmodel.MyViewModelFactory;

import java.util.UUID;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AdminCityListActivity extends AppCompatActivity {
    private CityAdapter cityAdapter;
    private ActivityAdminCityListBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<City> cities;
    @Inject
    MyViewModelFactory viewModelFactory;
    private AdminCityListViewModel adminCityListViewModel;
    private City currentCity = null;
    private Accommodation currentAccom = null;
    private int currentAction = ACTION_EDIT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_city_list);

        getDataFromIntent();
        initInjec();
        setUpRecycleView();
        if (currentAction == ACTION_EDIT){
            addEvents();
        }
        handleBack();

    }
    private void getDataFromIntent(){
        Intent i = getIntent();
        String idCity = i.getStringExtra("idCity");
        String idAccom = i.getStringExtra("idAccom");
        String nameCity = i.getStringExtra("nameCity");
        String nameAccom = i.getStringExtra("nameAccom");
        currentAction = i.getIntExtra("action", ACTION_EDIT);
        if (idAccom != null) {
            currentCity = new City(idCity, nameCity);
            currentAccom = new Accommodation(idAccom, nameAccom, 0);
            binding.txvTitle.setText("Chọn thành phố muốn chuyển");
        }
    }
    private void initInjec(){
        MainApplication mainApplication = (MainApplication) getApplication();
        ActivityComponent activityComponent =mainApplication.getActivityComponent();
        realmHelper.openRealm();
      //  adminCityListViewModel = new ViewModelProvider(this, viewModelFactory).get(AdminCityListViewModel.class);
    }
    private void setUpRecycleView(){
        binding.rcvCity.setLayoutManager(new GridLayoutManager(this, 3));
        cities = adminCityListViewModel.getCities();
        cityAdapter = new CityAdapter(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(City city) {
                if (currentAction == ACTION_EDIT){
                    Intent intent = new Intent(AdminCityListActivity.this, AdminAccomListCityActivity.class);
                    intent.putExtra("idCity", city.getIdCity());
                    intent.putExtra("name", city.getName());
                    startActivity(intent);
                }else {
                    if (city.getIdCity().equals(currentCity.getIdCity())) {
                        Toast.makeText(AdminCityListActivity.this, "Chỗ ở hiện tại đang ở thành phố này rồi", Toast.LENGTH_SHORT).show();
                    }else{
                        callMoveDialog(city);
                    }
                }
            }
        }, new CityAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(City city) {
                if (currentAction == ACTION_EDIT){
                    showBottomSheet(city);
                }
            }
        });
        binding.rcvCity.setAdapter(cityAdapter);
        cityAdapter.submitList(cities);
        cityAdapter.notifyDataSetChanged();
    }
    private void addEvents(){
        cities.addChangeListener(new RealmChangeListener<RealmResults<City>>() {
            @Override
            public void onChange(RealmResults<City> phongBans) {
                cityAdapter.submitList(cities);
                cityAdapter.notifyDataSetChanged();
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminCityListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminCityListActivity.this);
                builder.setTitle("Thêm thành phố");
                final View alertView = layoutInflater.inflate(R.layout.dialog_edit_city, null);
                builder.setView(alertView);
                DialogEditCityBinding editCityBinding = DialogEditCityBinding.inflate(getLayoutInflater());
                builder.setView(editCityBinding.getRoot());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String ten = editCityBinding.edtNameCity.getText().toString();
                        if (ten.equals("")){
                            Toast.makeText(AdminCityListActivity.this, "Tên thành phố không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String id = UUID.randomUUID().toString();
                        City city = new City(id, ten);
                        adminCityListViewModel.addCity(city);
                        Toast.makeText(AdminCityListActivity.this, "Thêm thành phố thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
    private void handleBack(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAction == ACTION_EDIT){
                    Intent intent = new Intent(AdminCityListActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                }else if (currentAction == ACTION_ADD_ACCOM){
                    Intent intent = new Intent(AdminCityListActivity.this, AdminAccomListActivity.class);
                    startActivity(intent);
                }else if (currentAction == ACTION_MOVE_ACCOM){
                    onBackPressed();
                }
            }
        });
    }
    public void showBottomSheet(City city){
        final Dialog dialog=new Dialog(AdminCityListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BottomSheetCityBinding cityBinding = BottomSheetCityBinding.inflate(getLayoutInflater());
        dialog.setContentView(cityBinding.getRoot());

        cityBinding.txvCityName.setText(city.getName());
        cityBinding.tbrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminCityListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(AdminCityListActivity.this);
                builder.setTitle("Chỉnh sửa tên thành phố");
                final View alertView = layoutInflater.inflate(R.layout.dialog_edit_city, null);
                builder.setView(alertView);
                DialogEditCityBinding editCityBinding = DialogEditCityBinding.inflate(getLayoutInflater());
                builder.setView(editCityBinding.getRoot());
                editCityBinding.edtNameCity.setText(city.getName());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String tenNew = editCityBinding.edtNameCity.getText().toString();
                        if (tenNew.equals("")){
                            Toast.makeText(AdminCityListActivity.this, "Tên thành phố không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        City ct = new City(city.getIdCity(), tenNew);
                        adminCityListViewModel.UpdateCity(ct);
                        Toast.makeText(AdminCityListActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        cityBinding.tbrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCityListActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        adminCityListViewModel.deleteCity(city.getIdCity());
                        Toast.makeText(AdminCityListActivity.this, "Xóa thành phố thành công", Toast.LENGTH_SHORT).show();
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
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void callMoveDialog(City city){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCityListActivity.this);
        builder.setTitle("Xác nhận chuyển");
        builder.setMessage("Bạn có chắc chắn muốn chuyển "+ currentAccom.getName() +" tới thành phố "+city.getName()+" không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogmini, int which) {
                adminCityListViewModel.addAccomToCity(currentAccom.getAccommodationId(), city.getIdCity());
                Toast.makeText(AdminCityListActivity.this, "Chuyển thành công", Toast.LENGTH_SHORT).show();
                if (currentAction == ACTION_ADD_ACCOM){
                    Intent intent = new Intent(AdminCityListActivity.this, AdminAccomListActivity.class);
                    startActivity(intent);
                }else if (currentAction == ACTION_MOVE_ACCOM){
                    Intent intent = new Intent(AdminCityListActivity.this, AdminAccomListCityActivity.class);
                    intent.putExtra("idCity", currentCity.getIdCity());
                    intent.putExtra("name", currentCity.getName());
                    startActivity(intent);
                }
                dialogmini.dismiss();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cities != null) {
            cities.removeAllChangeListeners();
        }
        realmHelper.closeRealm();
    }
}