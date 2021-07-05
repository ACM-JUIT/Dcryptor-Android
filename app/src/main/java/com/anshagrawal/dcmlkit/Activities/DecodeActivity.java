package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.databinding.ActivityDecodeBinding;

import java.util.ArrayList;

public class DecodeActivity extends AppCompatActivity {

    ActivityDecodeBinding binding;
    ArrayList<String> decodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        decodes = new ArrayList<>();

        binding.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myListView.setAdapter(null);
                decodes.clear();
                binding.trash.setVisibility(View.GONE);
            }
        });

        binding.empty.setBackgroundResource(R.drawable.rounded_corner);
        binding.empty.setClipToOutline(true);

        binding.myListView.setEmptyView(binding.empty);
    }
}