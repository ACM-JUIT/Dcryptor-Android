package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.databinding.ActivityAfterSplashScreenBinding;

public class AfterSplashScreenActivity extends AppCompatActivity {

    ActivityAfterSplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAfterSplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AfterSplashScreenActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AfterSplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}