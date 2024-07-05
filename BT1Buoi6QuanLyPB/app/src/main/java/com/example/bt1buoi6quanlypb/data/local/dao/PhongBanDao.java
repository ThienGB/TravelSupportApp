package com.example.bt1buoi6quanlypb.data.local.dao;

import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.RealmResults;

public interface PhongBanDao {
    void themPhongBan(PhongBan phongBan);
    void capNhatPhongBan(PhongBan phongBan);
    void xoaPhongBan(String maPhongBan);
    void themNhanVien(NhanVien nhanVien, String maPhongBan);
//    void capNhatNhanVien(NhanVien nhanVien, String maPhongBan);
//    void xoaNhanVien(String maNhanVien, String maPhongBan);
    RealmResults<PhongBan> layDSPB();
    RealmResults<PhongBan> layPB(String maPhongBan);
}
