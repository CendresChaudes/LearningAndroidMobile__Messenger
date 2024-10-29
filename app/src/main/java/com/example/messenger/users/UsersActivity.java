package com.example.messenger.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.login.LoginActivity;

public class UsersActivity extends AppCompatActivity {

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
        if (item.getItemId() == R.id.menuItemLogout) {
            this.launchLoginScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);
    }

    private void launchLoginScreen() {
        Intent intent = LoginActivity.createIntent(this);
        startActivity(intent);
    }
}