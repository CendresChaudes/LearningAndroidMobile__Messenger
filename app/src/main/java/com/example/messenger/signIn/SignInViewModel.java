package com.example.messenger.signIn;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInViewModel extends AndroidViewModel {

    private final FirebaseAuth auth;

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<FirebaseUser> user;

    public SignInViewModel(@NonNull Application application) {
        super(application);

        this.auth = FirebaseAuth.getInstance();

        this.errorMessage = new MutableLiveData<>();
        this.user = new MutableLiveData<>();

        this.setOnAuthStateListener();
    }

    public void signIn(String email, String password) {
        this.auth
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        user.setValue(null);
                        errorMessage.setValue(e.getMessage());
                    }
                });
    }

    public LiveData<String> getErrorMessage() {
        return this.errorMessage;
    }

    public LiveData<FirebaseUser> getUser() {
        return this.user;
    }

    private void setOnAuthStateListener() {
        this.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }
}
