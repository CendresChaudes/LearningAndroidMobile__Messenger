package com.example.messenger.users;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersViewModel extends AndroidViewModel {

    private final FirebaseAuth auth;

    private final MutableLiveData<FirebaseUser> user;

    public UsersViewModel(@NonNull Application application) {
        super(application);

        this.auth = FirebaseAuth.getInstance();
        this.user = new MutableLiveData<>();
        this.setOnAuthStateListener();
    }

    public void signOut() {
        this.auth.signOut();
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
