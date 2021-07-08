package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        decodes = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String[] decodedStringArray = bundle.getStringArray("decodedTextStringArray");
        arrayAdapter = new ArrayAdapter<String>(DecodeActivity.this, R.layout.activity_listview, decodedStringArray);
        binding.myListView.setAdapter(arrayAdapter);

//        VolleyResponseHandler volleyResponseHandler = new VolleyResponseHandler();

        decodes = new ArrayList<>();


//        binding.myListView.setBackgroundResource(R.drawable.edittext_listview);
//        binding.myListView.setClipToOutline(true);
    }


}