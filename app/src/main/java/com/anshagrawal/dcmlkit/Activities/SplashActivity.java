package com.anshagrawal.dcmlkit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.anshagrawal.dcmlkit.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        activitySplashBinding.logo.animate().rotation(180);
        getSupportActionBar().hide();
        //launch main activity after 3.2s os splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                finish();
            }

        }, 1750);
        activitySplashBinding.logo.animate().rotation(180).setDuration(1200);


    }

}