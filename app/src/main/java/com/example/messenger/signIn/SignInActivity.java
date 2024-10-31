package com.example.messenger.signIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.R;
import com.example.messenger.forgotPassword.ForgotPasswordActivity;
import com.example.messenger.signUp.SignUpActivity;
import com.example.messenger.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotPassword;
    private TextView textViewSignUp;
    private Button buttonSignIn;

    private SignInViewModel viewModel;

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        this.initActivity();
    }

    private void launchForgotPasswordScreen(String email) {
        Intent intent = ForgotPasswordActivity.createIntent(this, email);
        startActivity(intent);
    }

    private void launchSignUpScreen() {
        Intent intent = SignUpActivity.createIntent(this);
        startActivity(intent);
    }

    private void launchUsersScreen(String currentUserId) {
        Intent intent = UsersActivity.createIntent(this, currentUserId);
        startActivity(intent);
    }

    private void initActivity() {
        this.initViews();
        this.initViewModel();

        this.observeViewModel();
        this.setListeners();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        buttonSignIn = findViewById(R.id.buttonSignIn);
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    private void observeViewModel() {
        this.viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast
                            .makeText(
                                    SignInActivity.this,
                                    message,
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        this.viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    launchUsersScreen(user.getUid());
                    finish();
                }
            }
        });
    }

    private void setListeners() {
        this.setOnTextViewForgotPasswordClickListener();
        this.setOnTextViewSignUpClickListener();
        this.setOnButtonSignInClickListener();
    }

    private void setOnTextViewForgotPasswordClickListener() {
        this.textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getTrimmedStringFromEditText(editTextEmail);

                launchForgotPasswordScreen(email);
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
                String email = getTrimmedStringFromEditText(editTextEmail);
                String password = getTrimmedStringFromEditText(editTextPassword);

                boolean isInvalid = email.isEmpty() || password.isEmpty();

                if (isInvalid) {
                    Toast
                            .makeText(
                                    SignInActivity.this,
                                    R.string.fields_cannot_be_empty,
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    viewModel.signIn(email, password);
                }
            }
        });
    }

    @NonNull
    private String getTrimmedStringFromEditText(@NonNull EditText editText) {
        return editText.getText().toString().trim();
    }
}