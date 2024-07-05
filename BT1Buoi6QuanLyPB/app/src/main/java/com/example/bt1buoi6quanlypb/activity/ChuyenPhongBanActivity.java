package com.example.bt1buoi6quanlypb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import java.util.ArrayList;

public class ChuyenPhongBanActivity extends Activity {
    ListView lvPb;
    private static ArrayList<PhongBan> arrPhongBan = null;
    ArrayAdapter<PhongBan> adapter;
    ImageButton btnApply;
    NhanVien nv = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap_truong_phong);
        getFormWidgets();

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("DATA");
        nv = (NhanVien) b.getSerializable("NHANVIEN");
    }
    public void getFormWidgets(){
        lvPb = findViewById(R.id.lstPB);
        btnApply = findViewById(R.id.btnApplyCPB);
       // arrPhongBan = MainActivity.getListPhongBan();
        adapter = new ArrayAdapter<PhongBan>(this, android.R.layout.simple_list_item_single_choice, arrPhongBan);
        lvPb.setAdapter(adapter);
        lvPb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean somethongChecked = false;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (somethongChecked){
                    CheckedTextView cv = (CheckedTextView) view;
                    cv.setChecked((false));
                }
                CheckedTextView cv = (CheckedTextView)  view;
                if(!cv.isChecked()){
                    cv.setChecked(true);
                    arrPhongBan.get(position);
                }
                somethongChecked = true;
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doApply();
            }
        });
    }
    public void doApply(){
        //setResult(MainActivity.CHUYENPHONG_THANHCONG);
        finish();
    }
}
