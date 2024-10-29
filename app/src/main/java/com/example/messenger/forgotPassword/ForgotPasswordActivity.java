package com.example.messenger.forgotPassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
    }
}