package com.anshagrawal.dcmlkit.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anshagrawal.dcmlkit.BuildConfig;
import com.anshagrawal.dcmlkit.MySingleton;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.databinding.ActivityDecodeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DecodeActivity extends AppCompatActivity {
    ActivityDecodeBinding binding;
    ArrayList<String> decodes;
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog dialog;
    String[] str;
    SharedPreferencesClass sharedPreferences;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityDecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        decodes = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait... \n while we are processing your result");
        dialog.setCancelable(true);
        sharedPreferences = new SharedPreferencesClass(this);
        token = sharedPreferences.getValueString("token");
        Bundle bundle = getIntent().getExtras();
        String textToDecode = bundle.getString("textToDecode");
        boolean toStore = bundle.getBoolean("toStore");
        int toStoreInt;
        if(toStore==true){
            toStoreInt=1;
        }
        else{
            toStoreInt=0;
        }
        Log.d("@@@@", "response" + toStore);
        boolean method = bundle.getBoolean("method");
        String methodString ;
        if(method==true){
            methodString="base64";
        }
        else{
            methodString="recursive";
        }
        try {
            decodeCipher(textToDecode, toStoreInt, methodString);
            String[] decodedStringArray = str;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        decodes = new ArrayList<>();
    }

    //call the api to recursively decode the cipher
    private void decodeCipher(String text, int toStore, String method) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        try {
            dialog.show();
            jsonObject.put("data", text);
            jsonObject.put("toStore", toStore);
            Log.d("@@@@", "response  " + toStore);
            jsonObject.put("method", method);
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"https://acm-dcryptor.herokuapp.com/api/v2/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dialog.dismiss();
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
                Toast.makeText(DecodeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "Bearer "+token);
                return hashMap;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}