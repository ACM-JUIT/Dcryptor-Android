package com.anshagrawal.dcmlkit.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anshagrawal.dcmlkit.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }
}