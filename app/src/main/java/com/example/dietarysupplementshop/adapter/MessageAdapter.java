package com.example.dietarysupplementshop.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessageList;

    // Constructor
    public MessageAdapter(List<Message> messageList) {
        this.mMessageList = messageList;
    }

    // Provide a reference to the views for each data item
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public ImageView avatarImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textMessageBody);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Assuming your Message class has a method isSent to determine if the message is sent or received
        return mMessageList.get(position).isSent() ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // If the message is sent by the user
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent_layout, parent, false);
        } else {
            // If the message is received
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received_layout, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.messageText.setText(message.getText());
        // Here you can also set the avatar image using Glide or Picasso if you have different avatars for users.
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

