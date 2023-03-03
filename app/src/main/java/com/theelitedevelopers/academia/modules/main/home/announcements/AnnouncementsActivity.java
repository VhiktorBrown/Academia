package com.theelitedevelopers.academia.modules.main.home.announcements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.theelitedevelopers.academia.databinding.ActivityAnnouncementsBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Announcement;
import com.theelitedevelopers.academia.modules.main.home.announcements.adapters.AnnouncementListAdapter;

import java.util.ArrayList;

public class AnnouncementsActivity extends AppCompatActivity {
    ActivityAnnouncementsBinding binding;
    AnnouncementListAdapter adapter;
    ArrayList<Announcement> announcements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.announcementRecyclerView.setLayoutManager(layoutManager);
        binding.announcementRecyclerView.setHasFixedSize(true);

        populateDummyAnnouncements();

        adapter = new AnnouncementListAdapter(this, announcements);
        binding.announcementRecyclerView.setAdapter(adapter);

        binding.goBack.setOnClickListener(view -> onBackPressed());
    }

    private void populateDummyAnnouncements(){
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
    }
}