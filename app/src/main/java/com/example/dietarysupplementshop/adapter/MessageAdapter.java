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

    public MessageAdapter(List<Message> messageList) {
        this.mMessageList = messageList;
    }

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
        return mMessageList.get(position).isSent() ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received_layout, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.messageText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

