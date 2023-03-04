package com.theelitedevelopers.academia.modules.main.profile.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.profileName.setText(SharedPref.getInstance(requireActivity()).getString(Constants.NAME));
        binding.department.setText(SharedPref.getInstance(requireActivity()).getString(Constants.DEPARTMENT));
        binding.gender.setText(SharedPref.getInstance(requireActivity()).getString(Constants.GENDER));
        binding.phoneNumber.setText(SharedPref.getInstance(requireActivity()).getString(Constants.PHONE_NUMBER));
        binding.hostel.setText(SharedPref.getInstance(requireActivity()).getString(Constants.HOSTEL));
        binding.levelRegNumber.setText(SharedPref.getInstance(requireActivity()).getString(Constants.LEVEL) +"  "+
                SharedPref.getInstance(requireActivity()).getString(Constants.REG_NUMBER));


        return binding.getRoot();
    }
}