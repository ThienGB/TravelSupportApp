package com.example.bt1buoi6quanlypb.di.module;

import com.example.bt1buoi6quanlypb.data.model.NhanVien;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    List<PhongBan> provideFakePhongBan(){
        List<NhanVien> nv1 = new ArrayList<>(Arrays.asList(
            new NhanVien("nv1", "Hoàng Công Thiện", false),
            new NhanVien("nv2", "Phạm Bá Thành", false),
            new NhanVien("nv3", "Trương Hoàng Kim Ngân", true)
        ));
        List<NhanVien> nv2 = new ArrayList<>(Arrays.asList(
            new NhanVien("m1", "Phạm Công Hươởng", false),
            new NhanVien("m2", "Trần Kim Chon", false)
        ));
        List<PhongBan> pb = new ArrayList<>(Arrays.asList(
                new PhongBan("pb1","Kỹ Thuật"),
                new PhongBan("pb2","IT")
        ));
        return pb;
    }

}
