package com.example.bt1buoi6quanlypb.data.local.dao;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.Realm;
import io.realm.RealmResults;

public class NhanVienDaoImpl implements NhanVienDao{
    private Realm realm;
    private RealmHelper realmHelper;
    public NhanVienDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void themOrCapNhatNhanVien(NhanVien nhanVien) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(nhanVien);
        });
    }
    @Override
    public void xoaNhanVien(String id) {
        realm.executeTransactionAsync(r -> {
            PhongBan phongBan = r.where(PhongBan.class).equalTo("ma", id).findFirst();
            if (phongBan != null) {
                phongBan.deleteFromRealm();
            }
        });
    }

    @Override
    public RealmResults<NhanVien> layDSNV() {
        RealmResults<NhanVien> realmResults = realm.where(NhanVien.class).findAll();
        return realmResults;
    }

    @Override
    public RealmResults<NhanVien> layDSNVbyPB(String maPhongBan) {
        RealmResults<NhanVien> realmResults = realm.where(NhanVien.class).equalTo("ma", maPhongBan).findAll();
        return realmResults;
    }

    @Override
    public RealmResults<NhanVien> layNV(String id) {
        RealmResults<NhanVien> realmResults = realm.where(NhanVien.class).equalTo("ma", id).findAll();
        return realmResults;
    }
}
