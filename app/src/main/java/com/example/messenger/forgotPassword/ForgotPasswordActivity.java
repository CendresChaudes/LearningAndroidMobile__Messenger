package com.example.messenger.forgotPassword;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_KEY_EMAIL = "email";

    private EditText editTextEmail;
    private Button buttonResetPassword;

    private ForgotPasswordViewModel viewModel;

    @NonNull
    public static Intent createIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_EMAIL, email);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        this.initActivity();
    }

    private void initActivity() {
        this.initViews();
        this.initViewModel();

        this.observeViewModel();
        this.setOnButtonResetPasswordClickListener();
        this.setTextOnEditTextEmail();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
    }

    private void observeViewModel() {
        this.viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            ForgotPasswordActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        this.viewModel.getIsSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(
                            ForgotPasswordActivity.this,
                            R.string.message_successfully_sent,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void setTextOnEditTextEmail() {
        String email = getIntent().getStringExtra(INTENT_EXTRA_KEY_EMAIL);
        this.editTextEmail.setText(email);
    }

    private void setOnButtonResetPasswordClickListener() {
        this.buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getTrimmedStringFromEditText(editTextEmail);
                boolean isInvalid = email.isEmpty();

                if (isInvalid) {
                    Toast.makeText(
                            ForgotPasswordActivity.this,
                            R.string.fields_cannot_be_empty,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.resetPassword(email);
                }
            }
        });
    }

    @NonNull
    private String getTrimmedStringFromEditText(@NonNull EditText editText) {
        return editText.getText().toString().trim();
    }
}