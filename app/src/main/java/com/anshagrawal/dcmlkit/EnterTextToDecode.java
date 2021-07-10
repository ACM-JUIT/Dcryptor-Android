package com.anshagrawal.dcmlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.Activities.DecodeActivity;
import com.anshagrawal.dcmlkit.Activities.MainActivity;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityEnterTextToDecodeBinding;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EnterTextToDecode extends AppCompatActivity {
    CypherViewModel cypherViewModel;
    ActivityEnterTextToDecodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityEnterTextToDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MainActivity mainActivity = new MainActivity();
        VolleyResponseHandler volleyResponseHandler = new VolleyResponseHandler();
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