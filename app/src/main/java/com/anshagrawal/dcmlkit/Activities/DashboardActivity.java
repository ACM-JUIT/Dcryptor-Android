package com.anshagrawal.dcmlkit.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
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
import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.EnterTextToDecode;
import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.PickImageFromGallery;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityDashboardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    MainActivity mainActivity;
    ActivityDashboardBinding binding;
    CypherViewModel cypherViewModel;
    Uri imageUri;
    String token;
    CypherAdapter adapter;
    List<Dcryptor> filterDcryptorallList;
    SharedPreferencesClass sharedPreferences;
    ArrayList<CypherModel> arrayList;

    private static final int IMAGE_PICK_GALLERY_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Dashboard");
        sharedPreferences =  new SharedPreferencesClass(this);

        token = sharedPreferences.getValueString("token");


        binding.selectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });



        //cypherViewModel = ViewModelProviders.of(this).get(CypherViewModel.class);
        binding.addCypherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });

//        cypherViewModel.getallCyphers.observe(this, dcryptors -> {
//            binding.cypherRecycler.setLayoutManager(new LinearLayoutManager(this));
//
//            adapter = new CypherAdapter(DashboardActivity.this, dcryptors);
//            binding.cypherRecycler.setAdapter(adapter);
//            filterDcryptorallList = dcryptors;
//
//        });
        binding.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EnterTextToDecode.class);
                startActivity(intent);
            }
        });

        binding.cypherRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.cypherRecycler.setHasFixedSize(true);
        getTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                imageUri = data.getData();
//                cropImage(imageUri);
                    Intent i =new Intent(DashboardActivity.this, PickImageFromGallery.class);
                    i.putExtra("imageUri", imageUri.toString());
                    startActivity(i);
            }
        }

    }

    private void getTask() {
        arrayList = new ArrayList<>();
        //binding.progressBar.setVisibility(View.VISIBLE);
        String url = "https://acm-dcryptor.herokuapp.com/api/v2/history";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")){
                        //Toast.makeText(DashboardActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = response.getJSONArray("decodedHistory");
                        Log.i("historyapi", "response" + jsonArray.toString());
                        for (int i = 0; i<jsonArray.length();i++){
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


                    }
                    //binding.progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("historyapi", "error" + error.getMessage());
                Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response  =error.networkResponse;
                if (error instanceof ServerError && response != null){
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject  jsonObject = new JSONObject(res);
                        Toast.makeText(DashboardActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization",  "Bearer "  + token);

                return headers;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search your work here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                DcryptorFilter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                 sharedPreferences.clearData();
                 startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                 finish();
        }

        return super.onOptionsItemSelected(item);
    }

//    private void DcryptorFilter(String newText) {
//        ArrayList<Dcryptor> FilterNames = new ArrayList<>();
//        for (Dcryptor dcryptor : this.filterDcryptorallList) {
//            if (dcryptor.cypherTitle.contains(newText) || dcryptor.cypherDate.contains(newText)) {
//                FilterNames.add(dcryptor);
//            }
//        }
//        this.adapter.searchDcryptor(FilterNames);
//    }



}