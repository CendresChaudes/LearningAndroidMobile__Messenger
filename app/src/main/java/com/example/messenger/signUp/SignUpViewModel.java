package com.example.messenger.signUp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.messenger.common.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpViewModel extends AndroidViewModel {

    private static final String USERS_COLLECTION_NAME = "users";

    private final FirebaseAuth auth;
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference usersCollectionRef;

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<FirebaseUser> user;

    public SignUpViewModel(@NonNull Application application) {
        super(application);

        this.auth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.usersCollectionRef = this.firebaseDatabase.getReference(USERS_COLLECTION_NAME);

        this.errorMessage = new MutableLiveData<>();
        this.user = new MutableLiveData<>();

        this.setOnAuthStateListener();
    }

    public void signUp(
            String email,
            String password,
            String name,
            String lastname,
            int age
    ) {
        this.auth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();

                        if (firebaseUser == null) {
                            return;
                        }

                        User user = new User(
                                firebaseUser.getUid(),
                                name,
                                lastname,
                                age,
                                false
                        );

                        usersCollectionRef.child(firebaseUser.getUid()).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
