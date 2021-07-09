package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.databinding.ActivitySignUpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.wLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
            }
        });

        binding.gottologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });


//        private void decodeCipher(String text) throws JSONException {
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            final int[] length = new int[1];
//
//            JSONObject jsonObject = new JSONObject();
//            try {
//                //dialog.show();
//                jsonObject.put("data", text);
//            } catch (JSONException e) {
//               // dialog.dismiss();
//                e.printStackTrace();
//            }
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        //dialog.show();
//                        JSONArray jsonArray = response.getJSONArray("decoded_data");
//                        StringBuilder finalDecodedText = new StringBuilder();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String s = jsonArray.getString(i);
//                            decodes.add(s);
//                            if (i < (jsonArray.length() - 1)) {
//                                finalDecodedText.append(jsonArray.getString(i)).append("\n");
//                            } else {
//                                finalDecodedText.append(jsonArray.getString(i));
//                            }
//
//                        }
//
//                        String[] strArray = new String[decodes.size()];
//                        for (int i = 0; i < decodes.size(); i++) {
//                            strArray[i] = decodes.get(i);
//                        }
//                        Intent intent = new Intent(MainActivity.this, DecodeActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putStringArray("decodedTextStringArray", strArray);
//                        bundle.putString("scannedTextFinal", text);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        CreateCypher(text);
//
//
////                    arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, strArray);
////                    activityMainBinding.myListView.setAdapter(arrayAdapter);
//                        //dialog.dismiss();
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dialog.dismiss();
//                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//    }
    }
}