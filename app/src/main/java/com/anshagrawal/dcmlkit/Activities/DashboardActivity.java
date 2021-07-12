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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.EnterTextToDecode;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.PickImageFromGallery;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.UtilsService.SharedPreferencesClass;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    MainActivity mainActivity;
    ActivityDashboardBinding binding;
    CypherViewModel cypherViewModel;
    Uri imageUri;
    CypherAdapter adapter;
    List<Dcryptor> filterDcryptorallList;
    SharedPreferencesClass sharedPreferences;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Dashboard");
        sharedPreferences =  new SharedPreferencesClass(this);


        binding.selectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });



        cypherViewModel = ViewModelProviders.of(this).get(CypherViewModel.class);
        binding.addCypherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });

        cypherViewModel.getallCyphers.observe(this, dcryptors -> {
            binding.cypherRecycler.setLayoutManager(new LinearLayoutManager(this));

            adapter = new CypherAdapter(DashboardActivity.this, dcryptors);
            binding.cypherRecycler.setAdapter(adapter);
            filterDcryptorallList = dcryptors;

        });
        binding.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EnterTextToDecode.class);
                startActivity(intent);
            }
        });
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
                DcryptorFilter(newText);
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

    private void DcryptorFilter(String newText) {
        //Log.e("@@@@@","NotesFilter: "+newText);
        ArrayList<Dcryptor> FilterNames = new ArrayList<>();
        for (Dcryptor dcryptor : this.filterDcryptorallList) {
            if (dcryptor.cypherTitle.contains(newText) || dcryptor.cypherDate.contains(newText)) {
                FilterNames.add(dcryptor);
            }
        }
        this.adapter.searchDcryptor(FilterNames);
    }



}