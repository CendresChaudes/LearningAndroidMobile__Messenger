package com.example.messenger.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.signUp.SignUpActivity;

public class UsersActivity extends AppCompatActivity {

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, UsersActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
    }
}