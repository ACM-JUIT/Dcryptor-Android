package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    CypherViewModel cypherViewModel;
    CypherAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cypherViewModel = ViewModelProviders.of(this).get(CypherViewModel.class);
        binding.addCypherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });

        cypherViewModel.getallCyphers.observe(this, dcryptors -> {
            binding.cypherRecycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CypherAdapter(DashboardActivity.this, dcryptors);
            binding.cypherRecycler.setAdapter(adapter);

        });
    }



}