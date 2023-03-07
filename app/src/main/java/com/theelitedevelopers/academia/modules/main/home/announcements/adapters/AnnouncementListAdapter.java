package com.theelitedevelopers.academia.modules.main.home.announcements.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.databinding.AnnouncementLayoutBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Announcement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AnnouncementListAdapter extends RecyclerView.Adapter<AnnouncementListAdapter.AnnouncementViewHolder> {
    Context context;
    ArrayList<Announcement> announcements;

    public AnnouncementListAdapter(Context context, ArrayList<Announcement> announcements){
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AnnouncementLayoutBinding binding = AnnouncementLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AnnouncementViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        holder.binding.announcementTitle.setText(announcements.get(position).getTitle());
        holder.binding.announcementDetails.setText(announcements.get(position).getDescription());
        holder.binding.announcer.setText("By "+announcements.get(position).getAnnouncerName());
        holder.binding.announcementDate.setText(
                AppUtils.Companion.convertDateToPresentableFormatWithOnlyDate(
                        AppUtils.Companion.fromTimeStampToString(
                                announcements.get(position).getDate().getSeconds()
                        )));
    }

    public void setList(ArrayList<Announcement> announcements){
        this.announcements = announcements;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        AnnouncementLayoutBinding binding;
        public AnnouncementViewHolder(@NonNull AnnouncementLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
