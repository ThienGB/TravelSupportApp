package com.example.mapdemo.ui.viewmodel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapdemo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private Application application;
    @Inject
    public LoginViewModel(FirebaseAuth firebaseAuth, Application application){
        this.firebaseAuth = firebaseAuth;
        this.application = application;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(application, gso);
    }
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
    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w(TAG, "Google sign in failed", e);
            errorLiveData.postValue("Google sign in failed");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userLiveData.postValue(user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        errorLiveData.postValue("Authentication Failed.");
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            userLiveData.postValue(null);
        });
    }
}
