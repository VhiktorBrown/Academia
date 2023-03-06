package com.theelitedevelopers.academia.modules.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivitySplashScreenBinding;
import com.theelitedevelopers.academia.modules.authentication.LoginActivity;
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
            Intent intent;
            if(SharedPref.getInstance(getApplicationContext()).getBoolean(Constants.HAS_BEEN_LAUNCHED)){
                intent = new Intent(this, LoginActivity.class);
            }else {
                intent = new Intent(this, OnBoardingActivity.class);
            }
            startActivity(intent);
            finish();
        }, 500);
    }
}