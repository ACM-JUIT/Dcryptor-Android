package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.anshagrawal.dcmlkit.databinding.ActivityEnterTextToDecodeBinding;

public class EnterTextToDecode extends AppCompatActivity {

    ActivityEnterTextToDecodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityEnterTextToDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToDecode = binding.CipherEditText.getText().toString();
                Intent intent = new Intent(EnterTextToDecode.this, DecodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("textToDecode", textToDecode);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


}