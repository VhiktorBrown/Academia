package com.theelitedevelopers.academia.modules.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);

        if(SharedPref.getInstance(getApplicationContext()).getBoolean(Constants.REP)) {
            binding.navigation.getMenu().clear();
            binding.navigation.inflateMenu(R.menu.menu_main_rep);
            navController.setGraph(R.navigation.rep_main_navigation);
        }else {
            binding.navigation.getMenu().clear();
            binding.navigation.inflateMenu(R.menu.menu_main);
            navController.setGraph(R.navigation.main_navigation);
        }
        NavigationUI.setupWithNavController(binding.navigation, navController);
    }
}