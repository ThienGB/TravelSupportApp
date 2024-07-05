package com.example.bt1buoi6quanlypb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import java.util.ArrayList;

public class ThietLapTruongPhongActivity extends Activity {
    ListView lvTruongPhong, lvPhoPhong;
    ArrayList<NhanVien> arrNvForTP = new ArrayList<NhanVien>();
    ArrayAdapter<NhanVien> adapterForTP;
    ArrayList<NhanVien> arrNvForPP = new ArrayList<NhanVien>();
    ArrayAdapter<NhanVien> adapterForPP;
    ImageButton btnApply;
    int lastChecked = -1;
    PhongBan pb = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap_truong_phong);
    }
}
