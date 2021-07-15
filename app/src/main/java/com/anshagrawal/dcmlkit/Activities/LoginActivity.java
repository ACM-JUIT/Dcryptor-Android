package com.anshagrawal.dcmlkit.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anshagrawal.dcmlkit.MySingleton;
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
    UtilService utilService;
    SharedPreferencesClass sharedPreferences;
    private ProgressDialog dialog;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait... \nWhile we are fetching your details");
        dialog.setCancelable(true);
        utilService = new UtilService();

        sharedPreferences = new SharedPreferencesClass(this);


        //Sign up button functionality added
        binding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });


        //login button functionality added
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, LoginActivity.this);

                email = binding.loginemail.getText().toString();
                password = binding.loginpassword.getText().toString();

                if (validate(v)) {
                    loginUser(v);
                }
            }
        });


    }


    //Calling of the api for signing in the user
    private void loginUser(View view) {

        final HashMap<String, String> params = new HashMap<>();
        dialog.show();
        params.put("email", email);
        params.put("password", password);


        String url = "https://acm-dcryptor.herokuapp.com/api/v1/login";
        //Json Object and Post method
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dialog.dismiss();
                    if (response.getBoolean("status")) {
                        String token = response.getString("token");
                        sharedPreferences.setValueString("token", token);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject jsonObject = new JSONObject(res);
                        Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            //Headers added to get in which format we are getting the data from the api in our case it is Json
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return params;
            }
        };
        //Setting of retry policy in case of any network error
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        //request add
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

    //To see that nothing is kept empty while giving email and password
    private boolean validate(View v1) {
        boolean isValid;


        if (!TextUtils.isEmpty(email)) {
            if (!TextUtils.isEmpty(password)) {

                isValid = true;

            } else {
                utilService.showSnackBar(v1, "Please enter password...");
                isValid = false;

            }
        } else {
            utilService.showSnackBar(v1, "Please enter email...");
            isValid = false;

        }

        return isValid;
    }

    //Token stored in shared preferences and onStart is to check whether there is token or not if token is existing direct the user to the Dashboard Activity
    @Override
    protected void onStart() {
        super.onStart();
        android.content.SharedPreferences preferences = getSharedPreferences("user_cypher", MODE_PRIVATE);
        if (preferences.contains("token")) {
            Log.d("}}}", sharedPreferences.getValueString("token"));
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }


}