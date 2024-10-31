package com.example.messenger.users;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.common.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;

    private OnUserItemClickListener onUserItemClickListener;

    public UsersAdapter() {
        this.users = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item,
                parent,
                false
        );

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = this.users.get(position);

        TextView textViewUserInfo = holder.getTextViewUserInfo();
        View viewIsOnline = holder.getViewIsOnline();

        textViewUserInfo.setText(
                String.format(
                        "%s %s, %d",
                        user.getName(),
                        user.getLastname(),
                        user.getAge()
                )
        );

        Drawable background = ContextCompat.getDrawable(
                holder.itemView.getContext(),
                this.getIsOnlineBackgroundResId(user.getIsOnline())
        );

        viewIsOnline.setBackground(background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserItemClickListener != null) {
                    onUserItemClickListener.onClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setOnUserItemClickListener(OnUserItemClickListener onUserItemClickListener) {
        this.onUserItemClickListener = onUserItemClickListener;
    }

    private int getIsOnlineBackgroundResId(boolean isOnline) {
        int id;

        if (isOnline) {
            id = R.drawable.circle_green;
        } else {
            id = R.drawable.circle_red;
        }

        return id;
    }

    interface OnUserItemClickListener {

        void onClick(User user);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserInfo;
        private View viewIsOnline;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.initViews();
        }

        private void initViews() {
            this.textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            this.viewIsOnline = itemView.findViewById(R.id.viewIsOnline);
        }

        public TextView getTextViewUserInfo() {
            return this.textViewUserInfo;
        }

        public View getViewIsOnline() {
            return this.viewIsOnline;
        }
    }
}
