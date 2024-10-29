package com.example.messenger.signUp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.forgotPassword.ForgotPasswordActivity;
import com.example.messenger.login.LoginActivity;
import com.example.messenger.users.UsersActivity;

import org.jetbrains.annotations.Contract;

public class SignUpActivity extends AppCompatActivity {

    private Button buttonSignUp;

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        this.initActivity();
    }

    private void launchLoginScreen() {
        Intent intent = LoginActivity.createIntent(this);
        startActivity(intent);
    }

    private void initActivity() {
        this.initViews();

        this.setOnButtonSignUpClickListener();
    }

    private void initViews() {
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    private void setOnButtonSignUpClickListener() {
        this.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginScreen();
            }
        });
    }
}