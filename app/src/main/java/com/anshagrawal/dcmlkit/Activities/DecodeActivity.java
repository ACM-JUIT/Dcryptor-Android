package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anshagrawal.dcmlkit.BuildConfig;
import com.anshagrawal.dcmlkit.MySingleton;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.databinding.ActivityDecodeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecodeActivity extends AppCompatActivity {
    ActivityDecodeBinding binding;
    ArrayList<String> decodes;
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog dialog;
    MainActivity mainActivity = new MainActivity();
    String[] str ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        decodes = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String textToDecode = bundle.getString("textToDecode");
        try {
            decodeCipher(textToDecode, true,false);
            String[] decodedStringArray = str;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        decodes = new ArrayList<>();
    }
    //call the api to recursively decode the cipher
    private void decodeCipher(String text,boolean toStore, boolean base64) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
//            dialog.show();
            jsonObject.put("data", text);
        } catch (JSONException e) {
//            dialog.dismiss();
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.LINK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    dialog.show();
                    JSONArray jsonArray = response.getJSONArray("decoded_data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getString(i);
                        decodes.add(s);
                    }
                    String[] strArray = new String[decodes.size()];
                    for (int i = 0; i < decodes.size(); i++) {
                        strArray[i] = decodes.get(i);
                    }
                    arrayAdapter = new ArrayAdapter<String>(DecodeActivity.this, R.layout.activity_listview, strArray);
                    binding.myListView.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                Toast.makeText(DecodeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("token", "bcdb");
                return hashMap;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}