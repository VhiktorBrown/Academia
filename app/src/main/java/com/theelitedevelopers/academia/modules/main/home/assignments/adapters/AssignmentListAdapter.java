package com.theelitedevelopers.academia.modules.main.home.assignments.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theelitedevelopers.academia.databinding.AnnouncementLayoutBinding;
import com.theelitedevelopers.academia.databinding.AssignmentDetailsDialogBinding;
import com.theelitedevelopers.academia.databinding.AssignmentItemBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Announcement;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;

import java.util.ArrayList;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.AssignmentViewHolder> {

    Context context;
    ArrayList<Assignment> assignments;
    Dialog dialog;

    public AssignmentListAdapter(Context context, ArrayList<Assignment> assignments){
        this.context = context;
        this.assignments = assignments;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssignmentItemBinding binding = AssignmentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AssignmentViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        holder.binding.courseTitle.setText(assignments.get(position).getCourseName());
        holder.binding.courseCode.setText(assignments.get(position).getCourseCode());
        holder.binding.lecturer.setText(assignments.get(position).getLecturerName());
        holder.binding.assignmentTitle.setText(assignments.get(position).getTitle());
        holder.binding.assignmentDueDate.setText(assignments.get(position).getDateDue());

        holder.binding.getRoot().setOnClickListener(v -> showAssignmentDetails(assignments.get(position)));
    }

    public void setList(ArrayList<Assignment> assignments){
        this.assignments = assignments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        AssignmentItemBinding binding;
        public AssignmentViewHolder(@NonNull AssignmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showAssignmentDetails(Assignment assignment) {
        dialog = new Dialog(context);
        AssignmentDetailsDialogBinding dialogBinding = AssignmentDetailsDialogBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setCanceledOnTouchOutside(true);

        dialogBinding.courseCode.setText(assignment.getCourseCode());
        dialogBinding.courseTitle.setText(assignment.getCourseName());
        dialogBinding.assignmentTitle.setText(assignment.getTitle());
        dialogBinding.lecturer.setText(assignment.getLecturerName());
        dialogBinding.assignmentDueDate.setText(assignment.getDateDue());
        dialogBinding.assignmentDescription.setText("In full details and not less than 3 pages, give clear, easy to understand details about the assignment stated above in the title. To be submitted as a term paper");


        dialogBinding.goBack.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}
