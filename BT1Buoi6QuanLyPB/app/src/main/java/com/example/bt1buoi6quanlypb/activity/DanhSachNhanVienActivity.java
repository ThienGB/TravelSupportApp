package com.example.bt1buoi6quanlypb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;
import com.example.bt1buoi6quanlypb.databinding.ActivityDanhSachNhanVienBinding;
import com.example.bt1buoi6quanlypb.ui.adapter.NhanVienAdapter;

import java.util.ArrayList;

public class DanhSachNhanVienActivity extends Activity {
    ActivityDanhSachNhanVienBinding binding;
    ArrayList<NhanVien> arrNhanVien = null;
    NhanVienAdapter adapter = null;
    PhongBan pb = null;
    private NhanVien nvSelected = null;
    private int position = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_danh_sach_nhan_vien);
        getDataFromMain();
    }

    public void getDataFromMain() {
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String name = i.getStringExtra("name");
        pb.setMa(id);
        pb.setTenpb(name);
        //  arrNhanVien = pb.getListNhanVien();
        adapter = new NhanVienAdapter(this, R.layout.layout_item_custom, arrNhanVien);
        //  txtmsg.setText("DS nhân viên [" + pb.getTen()+"]");
    }



}
