package com.anshagrawal.dcmlkit.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.EnterTextToDecode;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    CypherViewModel cypherViewModel;
    CypherAdapter adapter;
    List<Dcryptor> filterDcryptorallList;
//    private static final int CAMERA_REQUEST_CODE = 200;
//    private static final int STORAGE_REQUEST_CODE = 400;
//    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
//    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

//    private void pickGallery() {
//        //intent to pick image from gallery
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        //set intent type to image
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //got image from camera
//        if (resultCode == RESULT_OK) {
//            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
//                //got image from gallery now crop it
//                CropImage.activity(data.getData())
//                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
//                        .start(this);
//            }
//            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
//                //got image from camera now crop it
//
//                CropImage.activity(image_uri)
//                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
//                        .start(this);
//            }
//        }
//        //get cropped image
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri(); //get image uri
//                //set image to image view
//                mPreviewIv.setImageURI(resultUri);
//
//                //get drawable bitmap for text recognition
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                //if there is any error show it
//                Exception error = result.getError();
//                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }

}