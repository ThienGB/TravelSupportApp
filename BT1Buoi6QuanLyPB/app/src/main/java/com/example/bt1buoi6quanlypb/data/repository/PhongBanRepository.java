package com.example.bt1buoi6quanlypb.data.repository;

import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.RealmResults;

public interface PhongBanRepository {
    void themPhongBan(PhongBan phongBan);
    void capNhatPhongBan(PhongBan phongBan);
    void xoaPhongBan(String maPhongBan);
    void themNhanVien(NhanVien nhanVien, String maPhongBan);
    RealmResults<PhongBan> layDSPB();
    RealmResults<PhongBan> layPB(String maPhongBan);
}
