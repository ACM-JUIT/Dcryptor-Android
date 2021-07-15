package com.anshagrawal.dcmlkit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.anshagrawal.dcmlkit.databinding.ActivitySplashBinding;

import java.util.concurrent.Executor;

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

        }, 2000);
        activitySplashBinding.logo.animate().rotation(180).setDuration(1200);


    }

}