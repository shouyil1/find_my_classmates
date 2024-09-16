package com.example.findMyClassmates;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private String currentUserEmail;

    // Constructor
    public MessagesAdapter(List<Message> messageList, Context context, String currentUserEmail) {
        this.messageList = messageList;
        this.context = context;
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList == null || position >= messageList.size()) {
            // Handle this case appropriately, perhaps by throwing an exception or logging an error
            return VIEW_TYPE_MESSAGE_RECEIVED; // Default to received as a fallback
        }
        Message message = messageList.get(position);
        if (message != null && message.isSentByCurrentUser(currentUserEmail)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        // Assuming you have a method to format the timestamp into a String
        String formattedTime = formatTimestamp(message.getTimestamp());

        holder.textViewMessage.setText(message.getContent());
        holder.textViewTime.setText(formattedTime);

        // Here you could set different layouts or text appearances based on senderId and receiverId
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // View holder for each message
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.messageTextView);
            textViewTime = itemView.findViewById(R.id.timeTextView);
        }
    }

    // Helper method to format the LocalDateTime
    private String formatTimestamp(Date timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        return formatter.format(timestamp); // Format the Date object to a String
    }

}
