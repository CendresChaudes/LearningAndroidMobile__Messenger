package com.example.messenger.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.messenger.common.Message;
import com.example.messenger.common.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String MESSAGES_COLLECTION_NAME = "messages";

    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference usersCollectionRef;
    private final DatabaseReference messagesCollectionRef;

    private final MutableLiveData<List<Message>> messages;
    private final MutableLiveData<User> theirUser;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isMessageSent;

    private final String currentUserId;
    private final String theirUserId;

    public ChatViewModel(
            String currentUserId,
            String theirUserId
    ) {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.usersCollectionRef = this.firebaseDatabase.getReference(USERS_COLLECTION_NAME);
        this.messagesCollectionRef = this.firebaseDatabase.getReference(MESSAGES_COLLECTION_NAME);

        this.currentUserId = currentUserId;
        this.theirUserId = theirUserId;
        this.messages = new MutableLiveData<>();
        this.theirUser = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        this.isMessageSent = new MutableLiveData<>();

        this.setListeners();
    }

    public void setUserIsOnline(Boolean isOnline) {
        this.usersCollectionRef
                .child(this.currentUserId)
                .child("isOnline")
                .setValue(isOnline);
    }

    public void sendMessage(@NonNull Message message) {
        this.messagesCollectionRef
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        messagesCollectionRef
                                .child(message.getReceiverId())
                                .child(message.getSenderId())
                                .push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        isMessageSent.setValue(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        errorMessage.setValue(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorMessage.setValue(e.getMessage());
                    }
                });
    }

    public LiveData<List<Message>> getMessages() {
        return this.messages;
    }

    public LiveData<User> getTheirUser() {
        return this.theirUser;
    }

    public LiveData<String> getErrorMessage() {
        return this.errorMessage;
    }

    public LiveData<Boolean> getIsMessageSent() {
        return this.isMessageSent;
    }

    private void setListeners() {
        this.setOnTheirUserAddValueListener();
        this.setOnMessagesAddValueListener();
    }

    private void setOnTheirUserAddValueListener() {
        this.usersCollectionRef
                .child(this.theirUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        theirUser.setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorMessage.setValue(error.getMessage());
                    }
                });
    }

    private void setOnMessagesAddValueListener() {
        this.messagesCollectionRef
                .child(this.currentUserId)
                .child(this.theirUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messagesFromDb = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messagesFromDb.add(message);
                        }

                        messages.setValue(messagesFromDb);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorMessage.setValue(error.getMessage());
                    }
                });
    }
}
