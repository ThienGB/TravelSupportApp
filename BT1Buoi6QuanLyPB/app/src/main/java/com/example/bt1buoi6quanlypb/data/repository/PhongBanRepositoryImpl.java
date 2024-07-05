package com.example.bt1buoi6quanlypb.data.repository;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.data.local.dao.PhongBanDao;
import com.example.bt1buoi6quanlypb.data.local.dao.PhongBanDaoImpl;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import io.realm.RealmResults;

public class PhongBanRepositoryImpl implements PhongBanRepository {
    private PhongBanDao phongBanDao;

    public PhongBanRepositoryImpl(RealmHelper realmHelper) {
        phongBanDao = new PhongBanDaoImpl(realmHelper);
    }
    @Override
    public void themPhongBan(PhongBan phongBan){
        phongBanDao.themPhongBan(phongBan);
    }
    @Override
    public void capNhatPhongBan(PhongBan phongBan){
        phongBanDao.capNhatPhongBan(phongBan);
    }
    @Override
    public void xoaPhongBan(String maPhongBan){
        phongBanDao.xoaPhongBan(maPhongBan);
    }
    @Override
    public void themNhanVien(NhanVien nhanVien, String maPhongBan){
        phongBanDao.themNhanVien(nhanVien, maPhongBan);
    }
    @Override
    public RealmResults<PhongBan> layDSPB(){
        return phongBanDao.layDSPB();
    }
    @Override
    public RealmResults<PhongBan> layPB(String maPhongBan){
        return phongBanDao.layPB(maPhongBan);
    }
}
