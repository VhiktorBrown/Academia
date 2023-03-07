package com.theelitedevelopers.academia.modules.main.home.adapters;

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
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.databinding.AssignmentDetailsDialogBinding;
import com.theelitedevelopers.academia.databinding.AssignmentLayoutBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;

import java.util.ArrayList;

public class DueAssignmentsAdapter extends RecyclerView.Adapter<DueAssignmentsAdapter.DueAssignmentViewHolder> {
    ArrayList<Assignment> dueAssignments;
    Context context;
    Dialog dialog;

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

        if(dueAssignments.get(position).getDateDue() != null){
            holder.binding.assignmentDueDate.setText(AppUtils.Companion.getTimeInDaysOrWeeks(
                    AppUtils.Companion.fromTimeStampToString(dueAssignments.get(position).getDateDue().getSeconds())));
        }

        holder.binding.courseCode.setText(dueAssignments.get(position).getCourseCode());
        holder.binding.assignmentInfo.setText(dueAssignments.get(position).getTitle());

        holder.binding.getRoot().setOnClickListener(v -> showAssignmentDetails(dueAssignments.get(position)));
    }

    public void setList(ArrayList<Assignment> assignments){
        this.dueAssignments = assignments;
        notifyDataSetChanged();
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
        dialogBinding.courseTitle.setText(assignment.getCourseTitle());
        dialogBinding.assignmentTitle.setText(assignment.getTitle());
        dialogBinding.lecturer.setText(assignment.getLecturerName());
        dialogBinding.assignmentDueDate.setText(AppUtils.Companion.getTimeInDaysOrWeeks(
                AppUtils.Companion.fromTimeStampToString(assignment.getDateDue().getSeconds())));
        dialogBinding.assignmentDescription.setText(assignment.getDescription());


        dialogBinding.goBack.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
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
