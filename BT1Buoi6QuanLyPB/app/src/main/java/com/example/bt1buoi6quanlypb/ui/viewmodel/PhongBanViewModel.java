package com.example.bt1buoi6quanlypb.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.bt1buoi6quanlypb.activity.RealmHelper;
import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;
import com.example.bt1buoi6quanlypb.data.repository.PhongBanRepository;
import com.example.bt1buoi6quanlypb.data.repository.PhongBanRepositoryImpl;

import io.realm.RealmResults;

public class PhongBanViewModel extends ViewModel {
    private PhongBanRepository phongBanRepository;
    private RealmResults<PhongBan> danhSachPB;

    public PhongBanViewModel(RealmHelper realmHelper){
        phongBanRepository = new PhongBanRepositoryImpl(realmHelper);
        loadDanhSachPB();
    }
    private void loadDanhSachPB(){
        danhSachPB = phongBanRepository.layDSPB();
    }
    public RealmResults<PhongBan> getDanhSachPhongBan(){
        return danhSachPB;
    }
    public void themPhongBan(PhongBan phongBan){
        phongBanRepository.themPhongBan(phongBan);
    }
    public void capNhatPhongBan(PhongBan phongBan){
        phongBanRepository.capNhatPhongBan(phongBan);
    }
    public void xoaPhongBan(String maPhongBan){
        phongBanRepository.xoaPhongBan(maPhongBan);
    }
    public void themNhanVien(NhanVien nhanVien, String maPhongBan){
        phongBanRepository.themNhanVien(nhanVien, maPhongBan);
    }

}
