package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.VolleyResponseHandler;
import com.anshagrawal.dcmlkit.databinding.ActivityDecodeBinding;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DecodeActivity extends AppCompatActivity {
    ActivityDecodeBinding binding;
    ArrayList<String> decodes;
    ArrayAdapter<String> arrayAdapter;

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


//        setContentView(R.layout.activity_decode);
//        ImageView trash = findViewById(R.id.trash);
//        ListView myListView = findViewById(R.id.myListView);
        TextView empty = findViewById(R.id.empty);

        ListView myListView = findViewById(R.id.myListView);

        Bundle bundle = getIntent().getExtras();
//        String scannedText = bundle.getString("scannedText");
        String[] decodedStringArray=bundle.getStringArray("decodedTextStringArray");
//        String scannedText = getIntent().getStringExtra("scannedText");
        arrayAdapter = new ArrayAdapter<String>(DecodeActivity.this, R.layout.activity_listview, decodedStringArray);
        binding.myListView.setAdapter(arrayAdapter);
//        VolleyResponseHandler volleyResponseHandler = new VolleyResponseHandler();

        decodes = new ArrayList<>();


//        binding.trash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.myListView.setAdapter(null);
//                decodes.clear();
//                binding.trash.setVisibility(View.GONE);
//            }
//        });
//        binding.trash.setBackgroundResource(R.drawable.rounded_corner);
//        empty.setClipToOutline(true);
//        binding.myListView.setEmptyView(empty);


//        binding.myListView.setBackgroundResource(R.drawable.edittext_listview);
//        binding.myListView.setClipToOutline(true);
    }
}