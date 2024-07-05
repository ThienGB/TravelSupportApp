package com.example.bt1buoi6quanlypb.data.model;

import java.io.Serializable;

public class Infor implements Serializable {
    private static final long  serialVersionIUD = 1L;
    private String ma;
    private String ten;
    public Infor(String ma, String ten){
        super();
        this.ma = ma;
        this.ten = ten;
    }
    public Infor(){
        super();
    }
    @Override
    public String toString(){
        return this.ma +" - "+ this.ten;
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
}
