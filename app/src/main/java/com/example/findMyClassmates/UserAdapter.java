package com.example.findMyClassmates;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context; // Context to be used for starting activities

    // Constructor including Context
    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView, parent.getContext()); // Pass the context to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        String receiver_email = user.getEmail();
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(receiver_email);
        holder.roleTextView.setText(user.getUserType());

        holder.nameTextView.setOnClickListener(v -> holder.navigateToProfileActivity(receiver_email)); // Use holder's method to handle click
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView emailTextView;
        TextView roleTextView;
        Button sendMessageButton;
        private Context context; // Context in the ViewHolder

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            nameTextView = itemView.findViewById(R.id.userNameTextView);
            emailTextView = itemView.findViewById(R.id.userEmailTextView);
            roleTextView = itemView.findViewById(R.id.userRoleTextView);
            sendMessageButton = itemView.findViewById(R.id.sendMessageButton);
        }

        private void navigateToProfileActivity(String email) {
            Intent profileIntent = new Intent(context, ClassmatesProfile.class);
            profileIntent.putExtra("USER_EMAIL", email);
            context.startActivity(profileIntent);
        }


    }
}
