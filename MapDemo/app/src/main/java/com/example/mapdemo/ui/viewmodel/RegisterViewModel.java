package com.example.mapdemo.ui.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mapdemo.ui.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.Objects;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth firebaseAuth;
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> confirmPassword = new ObservableField<>();

    @Inject
    public RegisterViewModel(FirebaseAuth firebaseAuth){
        this.firebaseAuth =firebaseAuth;
    }
    public void handleSignUp(Context context){
        if (checkAllFields(context)){
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Create new account...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(Objects.requireNonNull(email.get()),
                    Objects.requireNonNull(password.get())).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name.get()).build();
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

    private boolean checkAllFields(Context context) {
        if (email.get() == null || name.get() == null
                || password.get() == null ||confirmPassword.get() == null
                || Objects.requireNonNull(email.get()).length() == 0
                || Objects.requireNonNull(password.get()).length() == 0
                || Objects.requireNonNull(name.get()).length() == 0) {
            Toast.makeText(context, "Please complete all information", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(email.get())).matches()) {
            Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.requireNonNull(password.get()).length() < 6) {
            Toast.makeText(context, "Password must be minimum 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Objects.equals(password.get(), confirmPassword.get())) {
            Toast.makeText(context, "Confirm password does not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
