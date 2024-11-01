package com.example.messenger.users;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.messenger.common.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private static final String USERS_COLLECTION_NAME = "users";

    private final FirebaseAuth auth;
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference usersCollectionRef;

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<FirebaseUser> user;
    private final MutableLiveData<List<User>> users;

    public UsersViewModel(@NonNull Application application) {
        super(application);

        this.auth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.usersCollectionRef = this.firebaseDatabase.getReference(USERS_COLLECTION_NAME);

        this.errorMessage = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.users = new MutableLiveData<>();

        this.setListeners();
    }

    public void setUserIsOnline(Boolean isOnline) {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            this.usersCollectionRef
                    .child(user.getUid())
                    .child("isOnline")
                    .setValue(isOnline);
        }
    }

    public void signOut() {
        this.setUserIsOnline(false);
        this.auth.signOut();
    }

    public LiveData<String> getErrorMessage() {
        return this.errorMessage;
    }

    public LiveData<FirebaseUser> getUser() {
        return this.user;
    }

    public LiveData<List<User>> getUsers() {
        return this.users;
    }

    private void setListeners() {
        this.setOnAuthStateListener();
        this.setOnUsersDataChangeListener();
    }

    private void setOnAuthStateListener() {
        this.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }

    private void setOnUsersDataChangeListener() {
        this.usersCollectionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser == null) {
                    return;
                }

                List<User> usersFromDb = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user == null) {
                        return;
                    }

                    if (!user.getId().equals(currentUser.getUid())) {
                        usersFromDb.add(user);
                    }
                }

                users.setValue(usersFromDb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorMessage.setValue(error.getMessage());
            }
        });
    }
}
