package com.theelitedevelopers.academia.modules.add_announcements_assignments;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
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

        binding.logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            removeDataToSharedPref();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
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

    private void removeDataToSharedPref(){
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.ID);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.NAME);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.EMAIL);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.REG_NUMBER);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.DATE_OF_BIRTH);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.DEPARTMENT);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.HOSTEL);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.LEVEL);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.REP);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.PHONE_NUMBER);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.GENDER);
        SharedPref.getInstance(getApplicationContext()).removeKeyValue(Constants.UID);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchAssignments();
    }
}