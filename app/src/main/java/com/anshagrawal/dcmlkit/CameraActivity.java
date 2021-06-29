package com.anshagrawal.dcmlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }
}