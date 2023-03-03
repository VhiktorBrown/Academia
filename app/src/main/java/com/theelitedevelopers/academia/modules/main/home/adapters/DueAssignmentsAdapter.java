package com.theelitedevelopers.academia.modules.main.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.databinding.AssignmentLayoutBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;

import java.util.ArrayList;

public class DueAssignmentsAdapter extends RecyclerView.Adapter<DueAssignmentsAdapter.DueAssignmentViewHolder> {
    ArrayList<Assignment> dueAssignments;
    Context context;

    public DueAssignmentsAdapter(Context context, ArrayList<Assignment> dueAssignments){
        this.context = context;
        this.dueAssignments = dueAssignments;
    }

    @NonNull
    @Override
    public DueAssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssignmentLayoutBinding binding = AssignmentLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new DueAssignmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DueAssignmentViewHolder holder, int position) {
        Picasso.get()
                .load(dueAssignments.get(position).getImage())
                .placeholder(R.drawable.academia_assignments)
                .into(holder.binding.assignmentImageView);

        if(dueAssignments.get(position).getDateDue() != null){
            holder.binding.assignmentDueDate.setText(dueAssignments.get(position).getDateDue());
        }else {
            holder.binding.assignmentDueDate.setText("2 days left");
        }

        holder.binding.courseCode.setText(dueAssignments.get(position).getCourseCode());
        holder.binding.assignmentInfo.setText(dueAssignments.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dueAssignments.size();
    }

    public static class DueAssignmentViewHolder extends RecyclerView.ViewHolder {

        AssignmentLayoutBinding binding;

        public DueAssignmentViewHolder(@NonNull AssignmentLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
