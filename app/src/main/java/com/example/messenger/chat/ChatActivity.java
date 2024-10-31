package com.example.messenger.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.common.Message;
import com.example.messenger.R;
import com.example.messenger.common.User;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_KEY_CURRENT_USER_ID = "currentUserId";
    private static final String INTENT_EXTRA_KEY_THEIR_USER_ID = "theirUserId";

    private TextView textViewUserInfo;
    private View viewIsOnline;
    private RecyclerView recyclerViewMessages;
    private MessagesAdapter messagesAdapter;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    private String currentUserId;
    private String theirUserId;

    @NonNull
    public static Intent createIntent(
            Context context,
            String currentUserId,
            String theirUserId
    ) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_CURRENT_USER_ID, currentUserId);
        intent.putExtra(INTENT_EXTRA_KEY_THEIR_USER_ID, theirUserId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        this.initActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.viewModel.setUserIsOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.viewModel.setUserIsOnline(false);
    }

    private void initActivity() {
        this.getDataByIntent();
        this.initViews();
        this.initViewModel();

        this.observeViewModel();
        this.setOnImageViewSendMessageClickListener();
    }

    private void getDataByIntent() {
        this.currentUserId = getIntent().getStringExtra(INTENT_EXTRA_KEY_CURRENT_USER_ID);
        this.theirUserId = getIntent().getStringExtra(INTENT_EXTRA_KEY_THEIR_USER_ID);
    }

    private void initViews() {
        this.textViewUserInfo = findViewById(R.id.textViewUserInfo);
        this.viewIsOnline = findViewById(R.id.viewIsOnline);
        this.editTextMessage = findViewById(R.id.editTextMessage);
        this.imageViewSendMessage = findViewById(R.id.imageViewSendMessage);

        this.messagesAdapter = new MessagesAdapter(this.currentUserId);
        this.recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        this.recyclerViewMessages.setAdapter(this.messagesAdapter);
    }

    private void initViewModel() {
        this.viewModelFactory = new ChatViewModelFactory(this.currentUserId, this.theirUserId);

        this.viewModel = new ViewModelProvider(this, this.viewModelFactory)
                .get(ChatViewModel.class);
    }

    private void observeViewModel() {
        this.viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });

        this.viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast
                            .makeText(ChatActivity.this, message, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        this.viewModel.getIsMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSent) {
                if (isSent) {
                    editTextMessage.setText("");
                }
            }
        });

        this.viewModel.getTheirUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    String userInfo = String.format(
                            "%s %s, %d",
                            user.getName(),
                            user.getLastname(),
                            user.getAge()
                    );

                    textViewUserInfo.setText(userInfo);

                    int backgroundId;

                    if (user.getIsOnline()) {
                        backgroundId = R.drawable.circle_green;
                    } else {
                        backgroundId = R.drawable.circle_red;
                    }

                    Drawable background = ContextCompat.getDrawable(
                            ChatActivity.this,
                            backgroundId
                    );

                    viewIsOnline.setBackground(background);
                }
            }
        });
    }

    private void setOnImageViewSendMessageClickListener() {
        this.imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(
                        editTextMessage.getText().toString().trim(),
                        currentUserId,
                        theirUserId
                );

                viewModel.sendMessage(message);
            }
        });
    }
}