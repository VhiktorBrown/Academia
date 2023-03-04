package com.theelitedevelopers.academia.modules.main.home.assignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.theelitedevelopers.academia.databinding.ActivityAssignmentsBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.home.assignments.adapters.AssignmentListAdapter;

import java.util.ArrayList;

public class AssignmentsActivity extends AppCompatActivity {
    ActivityAssignmentsBinding binding;
    AssignmentListAdapter adapter;
    ArrayList<Assignment> assignments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.assignmentRecyclerView.setLayoutManager(layoutManager);
        binding.assignmentRecyclerView.setHasFixedSize(true);

        populateDummyAssignments();

        adapter = new AssignmentListAdapter(this, assignments);
        binding.assignmentRecyclerView.setAdapter(adapter);

        binding.goBack.setOnClickListener(view -> onBackPressed());
    }

    private void populateDummyAssignments(){
        assignments.add(new Assignment("CSC 401", "Data Structures and Algorithm", "Write on the types of Arrays there is.", "Prof. A. N. Anibogu", "15-03-2023"));
        assignments.add(new Assignment("CSC 421", "Introduction to Computer Graphics", "What is the history of Computer Graphics?", "Mr. chinedu Ikedieze", "18-03-2023"));
        assignments.add(new Assignment("CSC 441", "Research Methodology & Design", "What are the various ways to measure data?", "Prof. Boniface", "10 days left"));
        assignments.add(new Assignment("CSC 401", "Data Structures and Algorithm", "Write on the types of Arrays there is.", "Prof. A. N. Anibogu", "15-03-2023"));
        assignments.add(new Assignment("CSC 401", "Data Structures and Algorithm", "Write on the types of Arrays there is.", "Prof. A. N. Anibogu", "15-03-2023"));
        assignments.add(new Assignment("CSC 401", "Data Structures and Algorithm", "Write on the types of Arrays there is.", "Prof. A. N. Anibogu", "15-03-2023"));
    }

}