package com.example.messenger.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.forgotPassword.ForgotPasswordActivity;
import com.example.messenger.signUp.SignUpActivity;
import com.example.messenger.users.UsersActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotPassword;
    private TextView textViewSignUp;
    private Button buttonSignIn;

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        this.initActivity();
    }

    private void launchForgotPasswordScreen() {
        Intent intent = ForgotPasswordActivity.createIntent(this);
        startActivity(intent);
    }

    private void launchSignUpScreen() {
        Intent intent = SignUpActivity.createIntent(this);
        startActivity(intent);
    }

    private void launchUsersScreen() {
        Intent intent = UsersActivity.createIntent(this);
        startActivity(intent);
    }

    private void initActivity() {
        this.initViews();

        this.setOnTextViewForgotPasswordClickListener();
        this.setOnTextViewSignUpClickListener();
        this.setOnButtonSignInClickListener();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        buttonSignIn = findViewById(R.id.buttonSignIn);
    }

    private void setOnTextViewForgotPasswordClickListener() {
        this.textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchForgotPasswordScreen();
            }
        });
    }

    private void setOnTextViewSignUpClickListener() {
        this.textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpScreen();
            }
        });
    }

    private void setOnButtonSignInClickListener() {
        this.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUsersScreen();
            }
        });
    }
}