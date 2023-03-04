package com.theelitedevelopers.academia.modules.add_announcements_assignments;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityAddAnnouncementsAssignmentsBinding;
import com.theelitedevelopers.academia.modules.authentication.LoginActivity;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.MainActivity;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.home.assignments.AssignmentsActivity;
import com.theelitedevelopers.academia.modules.main.home.assignments.adapters.AssignmentListAdapter;

import java.util.ArrayList;

public class AddAnnouncementsAssignmentsActivity extends AppCompatActivity {
    ActivityAddAnnouncementsAssignmentsBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ArrayList<Assignment> assignments = new ArrayList<>();
    AssignmentListAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAnnouncementsAssignmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.assignmentRecyclerView.setLayoutManager(layoutManager);
        binding.assignmentRecyclerView.setHasFixedSize(true);

        binding.studentName.setText(AppUtils.Companion.getFirstNameOnly(SharedPref.getInstance(getApplicationContext()).getString(Constants.NAME)));
        binding.studentLevelRegNumber.setText(SharedPref.getInstance(getApplicationContext()).getString(Constants.LEVEL)+" | "+
                SharedPref.getInstance(getApplicationContext()).getString(Constants.REG_NUMBER));
        binding.studentDepartment.setText(SharedPref.getInstance(getApplicationContext()).getString(Constants.DEPARTMENT));


        fetchAssignments();

        adapter = new AssignmentListAdapter(this, assignments);
        binding.assignmentRecyclerView.setAdapter(adapter);


        binding.seeAssignments.setOnClickListener(v -> startActivity(new Intent(this, AddAssignmentActivity.class)));

        binding.seeAnnouncements.setOnClickListener(v -> startActivity(new Intent(this, AddAnnouncementActivity.class)));
    }

    private void fetchAssignments(){
        database.collection("assignments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(!task.getResult().isEmpty()){
                            assignments.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                assignments.add(document.toObject(Assignment.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            adapter.setList(assignments);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchAssignments();
    }
}