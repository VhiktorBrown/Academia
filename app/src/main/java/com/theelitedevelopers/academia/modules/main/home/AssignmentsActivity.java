package com.theelitedevelopers.academia.modules.main.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.theelitedevelopers.academia.databinding.ActivityAssignmentsBinding;

public class AssignmentsActivity extends AppCompatActivity {
    ActivityAssignmentsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}