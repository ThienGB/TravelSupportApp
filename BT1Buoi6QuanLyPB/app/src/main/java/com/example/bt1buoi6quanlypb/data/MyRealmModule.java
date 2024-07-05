package com.example.bt1buoi6quanlypb.data;

import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.annotations.RealmModule;

@RealmModule(classes = { PhongBan.class, NhanVien.class })
public class MyRealmModule {
}
