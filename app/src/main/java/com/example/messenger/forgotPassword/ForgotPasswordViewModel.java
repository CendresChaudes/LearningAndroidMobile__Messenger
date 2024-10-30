package com.example.messenger.forgotPassword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordViewModel extends AndroidViewModel {

    private final FirebaseAuth auth;

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isSuccess;

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);

        this.auth = FirebaseAuth.getInstance();

        this.errorMessage = new MutableLiveData<>();
        this.isSuccess = new MutableLiveData<>();
    }

    public void resetPassword(String email) {
        this.auth
                .sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isSuccess.setValue(true);
                        errorMessage.setValue(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isSuccess.setValue(false);
                        errorMessage.setValue(e.getMessage());
                    }
                });
    }

    public LiveData<String> getErrorMessage() {
        return this.errorMessage;
    }

    public LiveData<Boolean> getIsSuccess() {
        return this.isSuccess;
    }
}
