package com.example.bt1buoi6quanlypb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bt1buoi6quanlypb.MyApplication;
import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;
import com.example.bt1buoi6quanlypb.databinding.ActivityMainBinding;
import com.example.bt1buoi6quanlypb.di.component.AppComponent;
import com.example.bt1buoi6quanlypb.ui.adapter.PhongBanAdapter;
import com.example.bt1buoi6quanlypb.ui.adapter.PhongBanListAdapter;
import com.example.bt1buoi6quanlypb.ui.viewmodel.PhongBanViewModel;
import com.example.bt1buoi6quanlypb.ui.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private PhongBanAdapter adapterPb = null;
    private PhongBanListAdapter listPBAdapter;
    private PhongBan pbSelected = null;
    private ActivityMainBinding binding;
    @Inject
    RealmHelper realmHelper;
    RealmResults<PhongBan> danhSachPhongBan;
    @Inject
    ViewModelFactory viewModelFactory;
    private PhongBanViewModel phongBanViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MyApplication myApplication = (MyApplication) getApplication();
        AppComponent appComponent = myApplication.getAppComponent();
        appComponent.inject(this);
        phongBanViewModel = new ViewModelProvider(this, viewModelFactory).get(PhongBanViewModel.class);
        binding.rcvPB.setLayoutManager(new GridLayoutManager(this, 2));
        danhSachPhongBan = phongBanViewModel.getDanhSachPhongBan();
        listPBAdapter = new PhongBanListAdapter(new PhongBanListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PhongBan phongBan) {
                pbSelected = phongBan;
                Intent intent = new Intent(MainActivity.this, DanhSachNhanVienActivity.class);
                intent.putExtra("id", phongBan.getMa());
                intent.putExtra("name", phongBan.getTenpb());
//                binding.editMaPB.setText(pbSelected.getMa());
//                binding.editMaPB.setEnabled(false);
//                binding.editTenPB.setText(pbSelected.getTenpb());
            }
        });
        binding.rcvPB.setAdapter(listPBAdapter);
        addEvents();
        listPBAdapter.submitList(danhSachPhongBan);
    }
    public void addEvents(){
        binding.btnLuuPb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MaPB = binding.editMaPB.getText().toString();
                String TenPB = binding.editTenPB.getText().toString();
                if (MaPB.equals("") || TenPB.equals("")){
                }else {
                    PhongBan phongBan = new PhongBan(MaPB, TenPB);
                    phongBanViewModel.themPhongBan(phongBan);
                    binding.editMaPB.setText("");
                    binding.editTenPB.setText("");
                    binding.editMaPB.setEnabled(true);
                    pbSelected = null;
                }
            }
        });
        danhSachPhongBan.addChangeListener(new RealmChangeListener<RealmResults<PhongBan>>() {
            @Override
            public void onChange(RealmResults<PhongBan> phongBans) {
                listPBAdapter.submitList(danhSachPhongBan);
                listPBAdapter.notifyDataSetChanged();

            }
        });
        binding.btnXoaPb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pbSelected != null){
                    phongBanViewModel.xoaPhongBan(pbSelected.getMa());
                    binding.editMaPB.setText("");
                    binding.editTenPB.setText("");
                    binding.editMaPB.setEnabled(true);
                    pbSelected = null;
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (danhSachPhongBan != null) {
            danhSachPhongBan.removeAllChangeListeners();
        }
        realmHelper.closeRealm();
    }
}