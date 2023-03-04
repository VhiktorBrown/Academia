package com.theelitedevelopers.academia.modules.add_announcements_assignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.databinding.ActivityAddAnnouncementBinding;

public class AddAnnouncementActivity extends AppCompatActivity {
    ActivityAddAnnouncementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}