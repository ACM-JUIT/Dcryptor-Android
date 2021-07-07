package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.anshagrawal.dcmlkit.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        getSupportActionBar().hide();
//        setInitialYScale(activitySplashBinding.textViewD);
//        setInitialYScale(activitySplashBinding.textViewC);
//        setInitialYScale(activitySplashBinding.textViewR);
//        setInitialYScale(activitySplashBinding.textViewP);
//        setInitialYScale(activitySplashBinding.textViewT);
//        setInitialYScale(activitySplashBinding.textViewO);
//        setInitialYScale(activitySplashBinding.textViewR1);
//        setFinalYScale(activitySplashBinding.textViewD, 0);
//        setFinalYScale(activitySplashBinding.textViewC, 300);
//        setFinalYScale(activitySplashBinding.textViewR, 300 * 2);
//        setFinalYScale(activitySplashBinding.textViewP, 300 * 3);
//        setFinalYScale(activitySplashBinding.textViewT, 300 * 4);
//        setFinalYScale(activitySplashBinding.textViewO, 300 * 5);
//        setFinalYScale(activitySplashBinding.textViewR1, 300 * 6);
        activitySplashBinding.textViewY.animate().rotation(360).setDuration(800);

        //launch main activity after 3.2s os splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                finish();
            }
        }, 1000);
    }

    private void setInitialYScale(TextView textView) {
        textView.setY(1350);
    }

    private void setFinalYScale(TextView textView, long delay) {
        textView.animate().translationY(0).setDuration(400).setStartDelay(delay);
    }
}