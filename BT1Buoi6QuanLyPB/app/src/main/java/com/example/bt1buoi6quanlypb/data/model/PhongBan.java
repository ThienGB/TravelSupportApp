package com.example.bt1buoi6quanlypb.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PhongBan extends RealmObject {
    @PrimaryKey
    private String ma;
    private String tenpb;
    private RealmList<NhanVien> dsnv;

    public PhongBan(String ma, String tenpb, RealmList<NhanVien> dsnv) {
        this.ma = ma;
        this.tenpb = tenpb;
        this.dsnv = dsnv;
    }

    public PhongBan(String ma, String tenpb) {
        this.ma = ma;
        this.tenpb = tenpb;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenpb() {
        return tenpb;
    }

    public void setTenpb(String tenpb) {
        this.tenpb = tenpb;
    }

    public List<NhanVien> getDsnv() {
        return dsnv;
    }

    public void setDsnv(RealmList<NhanVien> dsnv) {
        this.dsnv = dsnv;
    }

    public PhongBan(){
        super();
    }

}
