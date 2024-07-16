package com.example.mapdemo.ui.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.ui.activity.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;
    @Inject
    public RegisterViewModel(FirebaseAuth firebaseAuth){
        this.firebaseAuth =firebaseAuth;
    }
    public void handleSignUp(String email, String password, String ten, Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Create new account...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(ten)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(context, "Đã có email gửi về, hãy verify", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(context, LoginActivity.class);
                                                        context.startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Cập nhật tên thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "ĐK thất bại", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }else {
                        Toast.makeText(context, "Địa chỉ email trùng", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}
