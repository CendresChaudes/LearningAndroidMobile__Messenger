package com.example.messenger.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.Message;
import com.example.messenger.R;
import com.example.messenger.users.UsersAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_THEIR_MESSAGE = 2;

    private List<Message> messages;
    private String currentUserId;

    public MessagesAdapter(String currentUserId) {
        this.messages = new ArrayList<>();
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = this.messages.get(position);
        int type;

        if (currentUserId.equals(message.getSenderId())) {
            type = VIEW_TYPE_MY_MESSAGE;
        } else {
            type = VIEW_TYPE_THEIR_MESSAGE;
        }

        return type;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;

        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            layoutResId = R.layout.my_message_item;
        } else {
            layoutResId = R.layout.their_message_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(
                layoutResId,
                parent,
                false
        );

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = this.messages.get(position);

        TextView textViewMessage = holder.getTextViewMessage();

        textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.initViews();
        }

        private void initViews() {
            this.textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }

        public TextView getTextViewMessage() {
            return this.textViewMessage;
        }
    }
}
