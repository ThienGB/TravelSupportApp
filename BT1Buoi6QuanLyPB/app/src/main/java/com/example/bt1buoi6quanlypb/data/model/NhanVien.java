package com.example.bt1buoi6quanlypb.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NhanVien extends RealmObject {
    @PrimaryKey
    private String ma;
    private String ten;
    private boolean gioitinh;
    private PhongBan phongban;

    public NhanVien(String ma, String ten, boolean gioitinh, PhongBan phongban) {
        this.ma = ma;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.phongban = phongban;
    }

    public NhanVien(String ma, String ten, boolean gioitinh) {
        this.ma = ma;
        this.ten = ten;
        this.gioitinh = gioitinh;
    }

    public NhanVien() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public boolean isGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(boolean gioitinh) {
        this.gioitinh = gioitinh;
    }

    public PhongBan getPhongban() {
        return phongban;
    }

    public void setPhongban(PhongBan phongban) {
        this.phongban = phongban;
    }
}
