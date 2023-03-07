package com.theelitedevelopers.academia.modules.main.chat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ChatLayoutBinding;
import com.theelitedevelopers.academia.databinding.StartNewChatLayoutBinding;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.chat.ChatActivity;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;

import java.util.ArrayList;

public class StartNewChatAdapter extends RecyclerView.Adapter<StartNewChatAdapter.StartNewChatViewHolder> {

    Context context;
    ArrayList<Student> students;

    public StartNewChatAdapter(Context context, ArrayList<Student> students){
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public StartNewChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StartNewChatLayoutBinding binding = StartNewChatLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new StartNewChatViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StartNewChatViewHolder holder, int position) {

        holder.binding.inboxName.setText(students.get(holder.getAdapterPosition()).getFullName());
        holder.binding.gender.setText(students.get(holder.getAdapterPosition()).getGender());


            holder.binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(Constants.RECEIVER_UID, students.get(position).getUid());
                intent.putExtra(Constants.NAME, students.get(position).getFullName());
                v.getContext().startActivity(intent);
            });
    }

    public void setList(ArrayList<Student> students){
        this.students = students;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StartNewChatViewHolder extends RecyclerView.ViewHolder {
        StartNewChatLayoutBinding binding;
        public StartNewChatViewHolder(@NonNull StartNewChatLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
