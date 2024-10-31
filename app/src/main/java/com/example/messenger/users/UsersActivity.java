package com.example.messenger.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.User;
import com.example.messenger.chat.ChatActivity;
import com.example.messenger.signIn.SignInActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_KEY_CURRENT_USER_ID = "currentUserId";

    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;

    private UsersViewModel viewModel;

    private String currentUserId;

    @NonNull
    public static Intent createIntent(
            Context context,
            String currentUserId
    ) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_CURRENT_USER_ID, currentUserId);

        return intent;
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

    private void launchLoginScreen() {
        Intent intent = SignInActivity.createIntent(this);
        startActivity(intent);
    }

    private void launchChatScreen(String theirUserId) {
        Intent intent = ChatActivity.createIntent(this, currentUserId, theirUserId);
        startActivity(intent);
    }

    private void initActivity() {
        this.getDataByIntent();
        this.initViews();
        this.initViewModel();

        this.observeViewModel();
        this.setOnUserItemClickListener();
    }

    private void getDataByIntent() {
        this.currentUserId = getIntent().getStringExtra(INTENT_EXTRA_KEY_CURRENT_USER_ID);
    }

    private void initViews() {
        this.usersAdapter = new UsersAdapter();
        this.recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        this.recyclerViewUsers.setAdapter(this.usersAdapter);
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    private void observeViewModel() {
        this.viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast
                            .makeText(
                                    UsersActivity.this,
                                    message,
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        this.viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user == null) {
                    launchLoginScreen();
                    finish();
                }
            }
        });

        this.viewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    usersAdapter.setUsers(users);
                }
            }
        });
    }

    private void setOnUserItemClickListener() {
        this.usersAdapter.setOnUserItemClickListener(new UsersAdapter.OnUserItemClickListener() {
            @Override
            public void onClick(User user) {
                launchChatScreen(user.getId());
            }
        });
    }
}