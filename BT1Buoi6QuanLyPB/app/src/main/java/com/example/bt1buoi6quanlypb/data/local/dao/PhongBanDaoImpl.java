package com.example.bt1buoi6quanlypb.data.local.dao;

import android.util.Log;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.Realm;
import io.realm.RealmResults;

public class PhongBanDaoImpl implements PhongBanDao{
    private Realm realm;
    private RealmHelper realmHelper;

    public PhongBanDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void themPhongBan(PhongBan phongBan){
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(phongBan);
        });
        Log.d("1", "Thêmmmmm");
    }
    @Override
    public void capNhatPhongBan(PhongBan phongBan){
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(phongBan);
        });
    }
    @Override
    public void xoaPhongBan(String maPhongBan){
        realm.executeTransactionAsync(r -> {
            PhongBan phongBan = r.where(PhongBan.class).equalTo("ma", maPhongBan).findFirst();
            if (phongBan != null) {
                phongBan.deleteFromRealm();
            }
        });
    }
    @Override
    public void themNhanVien(NhanVien nhanVien, String maPhongBan){
        realm.executeTransactionAsync(r -> {
            PhongBan phongBan = r.where(PhongBan.class).equalTo("ma", maPhongBan).findFirst();
            if (phongBan != null) {
                phongBan.getDsnv().add(nhanVien);
            }
        });
    }
    @Override
    public RealmResults<PhongBan> layDSPB(){
        RealmResults<PhongBan> realmResults = realm.where(PhongBan.class).findAll();
        return realmResults;
    }
    @Override
    public RealmResults<PhongBan> layPB(String maPhongBan){
        RealmResults<PhongBan> realmResults = realm.where(PhongBan.class).equalTo("ma", maPhongBan).findAll();
        return realmResults;
    }
}
