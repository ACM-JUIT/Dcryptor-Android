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
import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.Adapters.ResultAdapter;
import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.Models.ResultModel;
import com.anshagrawal.dcmlkit.MySingleton;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.databinding.ActivityResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    ArrayList<String> decodes;
    ResultAdapter adapter;
    SharedPreferencesClass sharedPreferences;
    String token;
    private ProgressDialog dialog;
    ArrayList<ResultModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        decodes = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait... \n while we are processing your result");
        dialog.setCancelable(true);
        sharedPreferences = new SharedPreferencesClass(this);
        token = sharedPreferences.getValueString("token");



        decodes = new ArrayList<>();
        try {
            decodeCipher();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Call the api to recursively decode the cipher
    private void decodeCipher() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        dialog.dismiss();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://acm-dcryptor.herokuapp.com/api/v2/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("decodedHistory");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = jsonArray.getString(i);
                        decodes.add(s);
                    }
                    String[] strArray = new String[decodes.size()];
                    for (int i = 0; i < decodes.size(); i++) {
                        strArray[i] = decodes.get(i);
                    }
                    //Log.d("!!!!", strArray[0]);
                    adapter = new ResultAdapter(ResultActivity.this, arrayList);

                    binding.resultRecycler.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "Bearer " + token);
                return hashMap;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}