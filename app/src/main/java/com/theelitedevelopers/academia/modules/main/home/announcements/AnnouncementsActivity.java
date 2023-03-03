package com.theelitedevelopers.academia.modules.main.home.announcements;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.theelitedevelopers.academia.databinding.ActivityAnnouncementsBinding;

public class AnnouncementsActivity extends AppCompatActivity {
    ActivityAnnouncementsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}