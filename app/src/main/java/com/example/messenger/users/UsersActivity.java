package com.example.messenger.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.R;
import com.example.messenger.signIn.SignInActivity;
import com.google.firebase.auth.FirebaseUser;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel viewModel;

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, UsersActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_activitity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemSignOut) {
            this.viewModel.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
        this.initActivity();
    }

    private void initActivity() {
        this.initViewModel();
        this.observeViewModel();
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    private void launchLoginScreen() {
        Intent intent = SignInActivity.createIntent(this);
        startActivity(intent);
    }

    private void observeViewModel() {
        this.viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user == null) {
                    launchLoginScreen();
                    finish();
                }
            }
        });
    }
}