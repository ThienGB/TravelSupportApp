package com.example.bt1buoi6quanlypb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;

public class SuaNhanVienActivity extends Activity {
    EditText editMa, editTen;
    RadioButton radNam;
    Button btnClear, btnSave;
    NhanVien nv = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);

    }

}
