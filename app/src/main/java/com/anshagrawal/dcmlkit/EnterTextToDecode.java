package com.anshagrawal.dcmlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.Activities.DecodeActivity;
import com.anshagrawal.dcmlkit.databinding.ActivityEnterTextToDecodeBinding;

import org.json.JSONException;

public class EnterTextToDecode extends AppCompatActivity {

    ActivityEnterTextToDecodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityEnterTextToDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        VolleyResponseHandler volleyResponseHandler= new VolleyResponseHandler();
        binding.decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToDecode = binding.CipherEditText.getText().toString();
                try {
                    String[] decoded = volleyResponseHandler.decodeCipher(textToDecode, EnterTextToDecode.this);
                    Log.d("bljj", textToDecode);
//                    Intent intent = new Intent(EnterTextToDecode.this, DecodeActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArray("decodedTextStringArray", decoded);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(EnterTextToDecode.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}