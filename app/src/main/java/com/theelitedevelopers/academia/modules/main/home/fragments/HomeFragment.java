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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.FragmentHomeBinding;
import com.theelitedevelopers.academia.modules.authentication.LoginActivity;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.home.adapters.DueAssignmentsAdapter;
import com.theelitedevelopers.academia.modules.main.home.announcements.AnnouncementsActivity;
import com.theelitedevelopers.academia.modules.main.home.assignments.AssignmentsActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    DueAssignmentsAdapter adapter;
    ArrayList<Assignment> dueAssignments = new ArrayList<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

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

//    private void populateDummyList(){
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
//    }

    private void fetchAssignments(){
        database.collection("assignments")
                .orderBy("dateDue", Query.Direction.ASCENDING)
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

    private void removeDataToSharedPref(){
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.ID);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.NAME);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.EMAIL);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.REG_NUMBER);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.DATE_OF_BIRTH);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.DEPARTMENT);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.HOSTEL);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.LEVEL);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.REP);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.PHONE_NUMBER);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.GENDER);
        SharedPref.getInstance(requireActivity()).removeKeyValue(Constants.UID);
    }
}