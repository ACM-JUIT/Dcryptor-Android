package com.anshagrawal.dcmlkit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.UtilsService.UtilService;
import com.anshagrawal.dcmlkit.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private String  email,password ;
    UtilService utilService;
    SharedPreferencesClass sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utilService = new UtilService();

        sharedPreferences = new SharedPreferencesClass(this);

        binding.withoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            }
        });
        binding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, LoginActivity.this);

                email = binding.loginemail.getText().toString();
                password = binding.loginPassword.getText().toString();


                if (validate(v)) {
                    loginUser(v);
                }
            }
        });


    }

    private void loginUser(View view){

        final HashMap<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("password", password);


        String url = "https://acm-dcryptor.herokuapp.com/api/v1/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")){
                        String token = response.getString("token");
                        sharedPreferences.setValueString("token", token);
                        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response  =error.networkResponse;
                if (error instanceof ServerError && response != null){
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject  jsonObject = new JSONObject(res);
                        Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return params;
            }
        };
        //setting of retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        //request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    private boolean validate(View v1) {
        boolean isValid;


            if(!TextUtils.isEmpty(email)){
                if(!TextUtils.isEmpty(password)){

                        isValid = true;

                }else{
                    utilService.showSnackBar(v1, "Please enter password...");
                    isValid = false;

                }
            }else{
                utilService.showSnackBar(v1, "Please enter email...");
                isValid = false;

            }

        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.content.SharedPreferences preferences = getSharedPreferences("user_cypher", MODE_PRIVATE);
        if (preferences.contains("token")){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }
}