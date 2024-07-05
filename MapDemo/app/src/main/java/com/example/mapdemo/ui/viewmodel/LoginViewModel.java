package com.example.mapdemo.ui.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    public interface UploadCallback {
        void onUploadComplete();
    }
    public void handleLogin(String email, String password, Context context, UploadCallback callback){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Login...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(context,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                callback.onUploadComplete();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(context,"You have not verified yet",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(context,"No account exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
