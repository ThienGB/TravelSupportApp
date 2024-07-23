package com.example.mapdemo.ui.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.example.mapdemo.helper.CallbackHelper;
import com.example.mapdemo.ui.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth firebaseAuth;
    @Inject
    public RegisterViewModel(FirebaseAuth firebaseAuth){
        this.firebaseAuth =firebaseAuth;
    }
    public void handleSignUp(String email, String password, String ten, Context context, CallbackHelper callback){
        if (checkAllFields(email, password, new CallbackHelper() {
            @Override
            public void onEmailError(String message) {
                callback.onEmailError(message);
            }
            @Override
            public void onPasswordError(String message) {
                callback.onPasswordError(message);
            }
        })){
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Create new account...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(ten).build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                user.sendEmailVerification().addOnCompleteListener(task11 -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "An email has been sent, please verify", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Name update failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "registration failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(context, "Duplicate email address", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private boolean checkAllFields(String email, String password, CallbackHelper callback) {
        if (email.length() == 0) {
            callback.onEmailError("This field is required");
            return false;
        }
        if (password.length() == 0) {
            callback.onPasswordError("This field is required");
            return false;
        }
        if (password.length() < 6) {
            callback.onPasswordError("Password must be minimum 6 characters");
            return false;
        }
        if (email.contains(" ")){
            callback.onEmailError("Spaces are not allowed");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError("Invalid email address");
            return false;
        }
        return true;
    }
}
