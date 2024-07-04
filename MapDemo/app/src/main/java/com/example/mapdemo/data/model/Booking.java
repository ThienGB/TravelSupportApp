package com.example.mapdemo.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Booking extends RealmObject {
    @PrimaryKey
    private String idBooking;
    private String idTarget;
    private String idUser;
    private Date startDay;
    private Date endDay;
    private int price;

    public Booking() {
    }

    public Booking(String idBooking, String idTarget, String idUser, Date startDay, Date endDay, int price) {
        this.idBooking = idBooking;
        this.idTarget = idTarget;
        this.idUser = idUser;
        this.startDay = startDay;
        this.endDay = endDay;
        this.price = price;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(String idTarget) {
        this.idTarget = idTarget;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
