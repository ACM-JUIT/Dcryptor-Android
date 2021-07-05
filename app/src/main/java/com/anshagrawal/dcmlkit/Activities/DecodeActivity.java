package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.databinding.ActivityDecodeBinding;

import java.util.ArrayList;

public class DecodeActivity extends AppCompatActivity {

    ActivityDecodeBinding binding;
    ArrayList<String> decodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        decodes = new ArrayList<>();

//        binding.trash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.myListView.setAdapter(null);
//                decodes.clear();
//                binding.trash.setVisibility(View.GONE);
//            }
//        });

//        binding.empty.setBackgroundResource(R.drawable.rounded_corner);
//        binding.empty.setClipToOutline(true);
//
//        binding.myListView.setEmptyView(binding.empty);
        //binding.scannedText.setMovementMethod(new ScrollingMovementMethod());
    }


}