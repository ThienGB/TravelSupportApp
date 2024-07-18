package com.example.mapdemo.ui.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.ui.activity.LoginActivity;
import com.example.mapdemo.ui.activity.UserHomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;
    private final GoogleSignInClient mGoogleSignInClient;
    public MutableLiveData<Boolean> isLogin = new MutableLiveData<>();
    public MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    @Inject
    public LoginViewModel(FirebaseAuth firebaseAuth, GoogleSignInClient mGoogleSignInClient){
        this.firebaseAuth = firebaseAuth;
        this.mGoogleSignInClient = mGoogleSignInClient;
    }
    public interface UploadCallback {
        void onUploadComplete();
    }
    public void handleLogin(String email, String password, Context context, UploadCallback callback){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Login...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
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
                });
    }
    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(Intent data, Context context) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account, context);
        } catch (ApiException e) {
            errorLiveData.postValue("Please select an account");
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Login...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isLogin.postValue(true);
                        progressDialog.dismiss();
                    } else {
                        errorLiveData.postValue("Authentication Failed.");
                        progressDialog.dismiss();
                    }
                });
    }
    public boolean checkIsLogin(SharedPreferences sharedPreferences, Context context){
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(context, UserHomeActivity.class);
            context.startActivity(intent);
        }
        return isLoggedIn;
    }
    public void setIsLogin(SharedPreferences sharedPreferences, Context context){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
        Intent intent= new Intent(context, UserHomeActivity.class);
        context.startActivity(intent);
    }
    public void signOut(SharedPreferences sharedPreferences, Context context) {
        firebaseAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> isLogin.postValue(false));
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
