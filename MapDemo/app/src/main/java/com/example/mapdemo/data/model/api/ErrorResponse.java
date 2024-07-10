package com.example.mapdemo.data.model.api;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(int code, String requestFailed) {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
