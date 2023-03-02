package com.theelitedevelopers.academia.modules.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.theelitedevelopers.academia.MainActivity;
import com.theelitedevelopers.academia.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logInButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });
    }
}