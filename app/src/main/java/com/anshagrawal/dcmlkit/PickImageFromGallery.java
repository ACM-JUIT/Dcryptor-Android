package com.anshagrawal.dcmlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anshagrawal.dcmlkit.Activities.MainActivity;
import com.anshagrawal.dcmlkit.databinding.ActivityPickImageFromGalleryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

public class PickImageFromGallery extends AppCompatActivity {

    ActivityPickImageFromGalleryBinding binding;
    Uri imageUri;
    MainActivity mainActivity;
    public static final int PICK_IMAGES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickImageFromGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        imageUri = Uri.parse(extras.getString("imageUri"));
        cropImage();
    }


    public void processBitmap(Bitmap bitmap, int rotation) {
        InputImage img = InputImage.fromBitmap(bitmap, rotation);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(img).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                Log.d("ggg", text.getText());
                binding.editText.setText(text.getText());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ggg", e.getMessage());
            }
        });
    }

    void cropImage() {
        CropImage.activity(imageUri).start(this);
    }


    @Override
    //crop image activity result listener, this block is executed when the image is cropped
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                binding.croppedImage.setImageURI(resultUri);
                Bitmap imageAfterCrop;
                try {
                    imageAfterCrop = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    processBitmap(imageAfterCrop, 0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}