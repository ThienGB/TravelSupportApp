package com.example.mapdemo.helper;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import javax.inject.Inject;

public class DialogHelper {
    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}