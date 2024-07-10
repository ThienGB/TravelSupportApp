package com.example.mapdemo.data.model;

public class FirebaseBooking {
    private String idBooking;
    private String idTarget;
    private String idUser;
    private long startDay;
    private long endDay;
    private int price;
    private int numOfRooms;
    public FirebaseBooking() {
    }

    public FirebaseBooking(String idBooking, String idTarget, String idUser, long startDay, long endDay, int price, int numOfRooms) {
        this.idBooking = idBooking;
        this.idTarget = idTarget;
        this.idUser = idUser;
        this.startDay = startDay;
        this.endDay = endDay;
        this.price = price;
        this.numOfRooms = numOfRooms;
    }

    public String getIdBooking() {
        return idBooking;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getStartDay() {
        return startDay;
    }

    public void setStartDay(long startDay) {
        this.startDay = startDay;
    }

    public long getEndDay() {
        return endDay;
    }

    public void setEndDay(long endDay) {
        this.endDay = endDay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }
}