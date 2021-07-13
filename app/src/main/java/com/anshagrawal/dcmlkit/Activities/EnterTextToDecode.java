package com.anshagrawal.dcmlkit.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anshagrawal.dcmlkit.R;
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

                //
                final boolean[] toStore = new boolean[1];
                final boolean[] method = new boolean[1];
                Dialog dialog1 = new Dialog(EnterTextToDecode.this);
                dialog1.setContentView(R.layout.dialog_resource);
                Button doneBtn = (Button) dialog1.findViewById(R.id.doneBtn);
                CheckBox checkBox = (CheckBox) dialog1.findViewById(R.id.checkBox);
                RadioButton radioButton1 = (RadioButton) dialog1.findViewById(R.id.radioButton);
                RadioButton radioButton2 = (RadioButton) dialog1.findViewById(R.id.radioButton2);
                if (checkBox.isChecked()) {
                    toStore[0] = true;

                } else {
                    toStore[0] = false;

                }
                if (radioButton1.isChecked()) {
                    method[0] = true;
                }

                if (radioButton2.isChecked()) {
                    method[0] = true;

                } else {
                    Toast.makeText(EnterTextToDecode.this, "Please check at least one method to decode", Toast.LENGTH_SHORT).show();
                }

                //
                    dialog1.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textToDecode = binding.CipherEditText.getText().toString();
                        Intent intent = new Intent(EnterTextToDecode.this, DecodeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("textToDecode", textToDecode);
                        bundle.putBoolean("toStore", toStore[0]);
                        bundle.putBoolean("method", method[0]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


            }
        });
    }


}