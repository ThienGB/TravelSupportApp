package com.example.bt1buoi6quanlypb.data.model;

public enum ChucVu {
    TruongPhong("Trưởng phòng"),
    PhoPhong("Phó phòng"),
    NhanVien("Nhân viên");
    private String cv;
    ChucVu(String cv){
        this.cv = cv;
    }
    public String getChucVu(){
        return this.cv;
    }
}
