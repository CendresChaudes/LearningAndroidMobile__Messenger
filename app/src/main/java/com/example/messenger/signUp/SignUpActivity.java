package com.example.messenger.signUp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.R;
import com.example.messenger.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastname;
    private EditText editTextAge;
    private Button buttonSignUp;

    private SignUpViewModel viewModel;

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

    private void launchUsersScreen() {
        Intent intent = UsersActivity.createIntent(this);
        startActivity(intent);
    }

    private void initActivity() {
        this.initViews();
        this.initViewModel();

        this.observeViewModel();
        this.setOnButtonSignUpClickListener();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextAge = findViewById(R.id.editTextAge);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    private void observeViewModel() {
        this.viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            SignUpActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        this.viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    launchUsersScreen();
                    finish();
                }
            }
        });
    }

    private void setOnButtonSignUpClickListener() {
        this.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getTrimmedStringFromEditText(editTextEmail);
                String password = getTrimmedStringFromEditText(editTextPassword);
                String name = getTrimmedStringFromEditText(editTextName);
                String lastname = getTrimmedStringFromEditText(editTextLastname);
                String age = getTrimmedStringFromEditText(editTextAge);

                boolean isSomeFieldEmpty =
                        email.isEmpty() ||
                                password.isEmpty() ||
                                name.isEmpty() ||
                                lastname.isEmpty() ||
                                age.isEmpty();


                int transformedAge;

                if (age.isEmpty()) {
                    transformedAge = 0;
                } else {
                    transformedAge = Integer.parseInt(age);
                }

                boolean isLowerAgeLimitInvalid = transformedAge < 16;
                boolean isUpperAgeLimitInvalid = transformedAge > 125;

                if (isSomeFieldEmpty) {
                    Toast.makeText(
                            SignUpActivity.this,
                            R.string.fields_cannot_be_empty,
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (isLowerAgeLimitInvalid) {
                    Toast.makeText(
                            SignUpActivity.this,
                            R.string.you_must_be_at_least_16_years_old,
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (isUpperAgeLimitInvalid) {
                    Toast.makeText(
                            SignUpActivity.this,
                            R.string.incorrect_age_specified,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.signUp(email, password, name, lastname, transformedAge);
                }
            }
        });
    }

    @NonNull
    private String getTrimmedStringFromEditText(@NonNull EditText editText) {
        return editText.getText().toString().trim();
    }
}