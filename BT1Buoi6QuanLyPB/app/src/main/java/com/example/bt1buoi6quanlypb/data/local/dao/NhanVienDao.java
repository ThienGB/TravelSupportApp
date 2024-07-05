package com.example.bt1buoi6quanlypb.data.local.dao;

import com.example.bt1buoi6quanlypb.data.model.NhanVien;

import io.realm.RealmResults;

public interface NhanVienDao {
    void themOrCapNhatNhanVien(NhanVien nhanVien);
    void xoaNhanVien(String id);
    RealmResults<NhanVien> layDSNV();
    RealmResults<NhanVien> layDSNVbyPB(String maPhongBan);
    RealmResults<NhanVien> layNV(String id);
}
