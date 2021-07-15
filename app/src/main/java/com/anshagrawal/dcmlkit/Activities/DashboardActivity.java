package com.anshagrawal.dcmlkit.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.Adapters.ResultAdapter;
import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.MySingleton;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.databinding.ActivityDashboardBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {


    ActivityDashboardBinding binding;
    Uri imageUri;
    String token;
    CypherAdapter adapter;
    ResultAdapter adapter1;
    private ProgressDialog dialog;
    SharedPreferencesClass sharedPreferences;
    ArrayList<CypherModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing your result");
        dialog.setCancelable(true);
        getSupportActionBar().setTitle("Dashboard");
        getTask();

        sharedPreferences = new SharedPreferencesClass(this);
        //Getting token that has been stored in shared preferences
        token = sharedPreferences.getValueString("token");

        //Selecting image from gallery
        binding.selectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        //Functionality to decode the cypher using camera of the device on clicking the cypher button
        binding.addCypherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });

        //By using only text mode this button will be used
        binding.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EnterTextToDecode.class);
                startActivity(intent);
            }
        });
        //Setting the recycler view
        dialog.show();
        binding.cypherRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.cypherRecycler.setHasFixedSize(true);
        getTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                imageUri = data.getData();
                Intent i = new Intent(DashboardActivity.this, PickImageFromGallery.class);
                i.putExtra("imageUri", imageUri.toString());
                startActivity(i);
            }
        }

    }

    //Api for fetching the data of a particular user
    private void getTask() {
        arrayList = new ArrayList<>();
        String url = "https://acm-dcryptor.herokuapp.com/api/v2/history";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dialog.dismiss();
                    if (response.getBoolean("status")) {
                        arrayList.clear();
                        JSONArray jsonArray = response.getJSONArray("decodedHistory");
                        if (jsonArray.length() == 0) {
                            binding.emptyCypher.setVisibility(View.VISIBLE);
                        } else {
                            binding.emptyCypher.setVisibility(View.GONE);

                            //Log.i("historyapi", "response" + jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                CypherModel cypherModel = new CypherModel(
                                        obj.getString("_id"),
                                        obj.getString("stringtoDecode"),
                                        obj.getString("decodedAt")
                                );

                                arrayList.add(cypherModel);

                            }

                            adapter = new CypherAdapter(DashboardActivity.this, arrayList);
                            binding.cypherRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    }


                    //arrayList.clear();
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                }


            }

        }, //Error Handling
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Log.i("historyapi", "error" + error.getMessage());
                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject jsonObject = new JSONObject(res);
                                Toast.makeText(DashboardActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }) {
            // Headers added to get in which format we are getting the data from the api in our case it is Json and
            // also authorization is also done when the api will get the token then it will send the data of the particular user.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);

                return headers;
            }


        };
        //Setting of retry policy in case of any network error
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        //Adding request queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    //Overriding the below methods for showing the menu on the top right of the dashboard activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                sharedPreferences.clearData();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.Refresh:
                adapter.notifyDataSetChanged();

                break;
            case R.id.Share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Hey try this Decode Cypher App. It decodes your cypher and save it securely and permanently.";
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share via"));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTask();
    }
}