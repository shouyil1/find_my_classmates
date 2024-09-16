package com.example.findMyClassmates;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ViewHolder> {
    private List<Chatroom> chatroomList;
    private Context context;

    public ChatroomAdapter(List<Chatroom> chatroomList, Context context) {
        this.chatroomList = chatroomList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatroom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chatroom chatroom = chatroomList.get(position);
        String chatroomName = chatroom.getChatroomName();
        String latestMsg = chatroom.getLastMessage();
        String latestTime = chatroom.getLastTimestamp();
        String email = chatroom.getRecipientEmail();

        holder.chatroomName.setText(chatroomName);
        holder.latestMessage.setText(latestMsg);
        holder.latestTimestamp.setText(latestTime);

        holder.itemView.setOnClickListener(v -> {
            // 进入聊天框
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("user_email", email);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatroomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chatroomName, latestMessage, latestTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatroomName = itemView.findViewById(R.id.chatroomName);
            latestMessage = itemView.findViewById(R.id.latestMessage);
            latestTimestamp = itemView.findViewById(R.id.latestTimestamp);
        }
    }
}
