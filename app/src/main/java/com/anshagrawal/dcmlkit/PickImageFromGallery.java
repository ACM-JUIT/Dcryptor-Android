package com.anshagrawal.dcmlkit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.anshagrawal.dcmlkit.databinding.ActivityPickImageFromGalleryBinding;
import com.theartofdev.edmodo.cropper.CropImage;

public class PickImageFromGallery extends AppCompatActivity {

    ActivityPickImageFromGalleryBinding binding;
    Uri imageUri;
    public static final int PICK_IMAGES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickImageFromGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                imageUri = data.getData();
                CropImage.activity(imageUri).start(this);

            }
        }
    }


}