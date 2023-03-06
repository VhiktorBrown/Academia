package com.theelitedevelopers.academia.modules.main.home.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.FragmentClassRepHomeBinding;
import com.theelitedevelopers.academia.modules.add_announcements_assignments.AddAnnouncementActivity;
import com.theelitedevelopers.academia.modules.add_announcements_assignments.AddAssignmentActivity;
import com.theelitedevelopers.academia.modules.authentication.LoginActivity;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.home.adapters.DueAssignmentsAdapter;
import com.theelitedevelopers.academia.modules.main.home.announcements.AnnouncementsActivity;
import com.theelitedevelopers.academia.modules.main.home.assignments.AssignmentsActivity;

import java.util.ArrayList;

public class ClassRepHomeFragment extends Fragment {
    FragmentClassRepHomeBinding binding;
    DueAssignmentsAdapter adapter;
    ArrayList<Assignment> dueAssignments = new ArrayList<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClassRepHomeBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        binding.dueAssignmentsRecyclerView.setLayoutManager(layoutManager);
        binding.dueAssignmentsRecyclerView.setHasFixedSize(true);

        //populateDummyList();
        fetchAssignments();

        adapter = new DueAssignmentsAdapter(requireActivity(), dueAssignments);
        binding.dueAssignmentsRecyclerView.setAdapter(adapter);

        binding.studentName.setText(AppUtils.Companion.getFirstNameOnly(SharedPref.getInstance(requireActivity()).getString(Constants.NAME)));
        binding.studentLevelRegNumber.setText(SharedPref.getInstance(requireActivity()).getString(Constants.LEVEL)+" | "+
                SharedPref.getInstance(requireActivity()).getString(Constants.REG_NUMBER));
        binding.studentDepartment.setText(SharedPref.getInstance(requireActivity()).getString(Constants.DEPARTMENT));

        binding.addAssignments.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AddAssignmentActivity.class));
        });

        binding.addAnnouncements.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AddAnnouncementActivity.class));
        });

        binding.seeAssignments.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AssignmentsActivity.class));
        });

        binding.seeAnnouncements.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AnnouncementsActivity.class));
        });

        binding.logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AppUtils.Companion.removeDataToSharedPref(requireActivity());
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            ((Activity) v.getContext()).finishAffinity();
        });

        return binding.getRoot();
    }

    private void fetchAssignments(){
        database.collection("assignments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(!task.getResult().isEmpty()){
                            dueAssignments.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dueAssignments.add(document.toObject(Assignment.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            adapter.setList(dueAssignments);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}