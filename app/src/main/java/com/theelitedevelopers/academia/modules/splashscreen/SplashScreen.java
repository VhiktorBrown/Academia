package com.theelitedevelopers.academia.modules.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.theelitedevelopers.academia.databinding.ActivitySplashScreenBinding;
import com.theelitedevelopers.academia.modules.onboarding.OnBoardingActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }, 500);
    }
}