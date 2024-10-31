package com.example.messenger.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private final String theirUserId;
    private final String currentUserId;

    public ChatViewModelFactory(String currentUserId, String theirUserId) {
        this.currentUserId = currentUserId;
        this.theirUserId = theirUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId, theirUserId);
    }
}
