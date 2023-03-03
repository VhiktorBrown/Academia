package com.theelitedevelopers.academia.modules.main.home.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.databinding.FragmentHomeBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.home.adapters.DueAssignmentsAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    DueAssignmentsAdapter adapter;
    ArrayList<Assignment> dueAssignments = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        binding.dueAssignmentsRecyclerView.setLayoutManager(layoutManager);
        binding.dueAssignmentsRecyclerView.setHasFixedSize(true);

        populateDummyList();

        adapter = new DueAssignmentsAdapter(requireActivity(), dueAssignments);
        binding.dueAssignmentsRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void populateDummyList(){
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
        dueAssignments.add(new Assignment("CSC 422", "Research on Rete Algorithm and write a term paper on it", "03 Apr"));
    }
}