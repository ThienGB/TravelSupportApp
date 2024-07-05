package com.example.bt1buoi6quanlypb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.bt1buoi6quanlypb.R;

public class ThemNhanVienActivity extends Activity {
    private Button btnXoaTrang, btnLuuNhanVien;
    private EditText edtManv, editTenNV;
    private RadioButton radNam;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
    }

}
