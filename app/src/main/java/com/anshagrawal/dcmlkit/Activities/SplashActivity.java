package com.anshagrawal.dcmlkit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.anshagrawal.dcmlkit.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;
    SharedPreferences sharedPreferencesClass;
    SharedPreferences.Editor editor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        activitySplashBinding.logo.animate().rotation(180);
        getSupportActionBar().hide();
        sharedPreferencesClass = this.getSharedPreferences("splash", MODE_PRIVATE);
        editor = sharedPreferencesClass.edit();
        //launch main activity after 3.2s os splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferencesClass.getBoolean("isMain", false)) {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));


                } else {
                    editor.putBoolean("isMain", true);
                    editor.apply();


                    startActivity(new Intent(SplashActivity.this, AfterSplashScreenActivity.class));
                }


            }

        }, 1750);

        activitySplashBinding.logo.animate().rotation(180).setDuration(1200);


    }

}