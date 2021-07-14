package com.anshagrawal.dcmlkit.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.anshagrawal.dcmlkit.AfterMainActivity;
import com.anshagrawal.dcmlkit.BuildConfig;
import com.anshagrawal.dcmlkit.R;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int rotationAfterCrop;
    com.anshagrawal.dcmlkit.databinding.ActivityMainBinding activityMainBinding;
    String url = BuildConfig.LINK;
    String cypher_title;
    String sTitle;
    ArrayList<String> decodes;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = com.anshagrawal.dcmlkit.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();


        sTitle = getIntent().getStringExtra("title");

        activityMainBinding.scannedText.setText(sTitle);
        dialog = new ProgressDialog(this);
        dialog.setMessage("We are processing your results. \n Please Wait...");
        dialog.setCancelable(false);


        decodes = new ArrayList<>();


        activityMainBinding.cameraView.setBackgroundResource(R.drawable.rounded_corner);
        activityMainBinding.cameraView.setClipToOutline(true);

        activityMainBinding.scannedText.setBackgroundResource(R.drawable.rounded_corner);
        activityMainBinding.scannedText.setClipToOutline(true);


        //makes the TextView scrollable
        activityMainBinding.scannedText.setMovementMethod(new ScrollingMovementMethod());


        //camera listener, listens the activity of camera
        activityMainBinding.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                rotationAfterCrop = result.getRotation();
                result.toBitmap(new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        try {
                            cropImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        activityMainBinding.cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        activityMainBinding.cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);

        activityMainBinding.btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editedFinal = activityMainBinding.scannedText.getText().toString();

                try {
                    decodeCipher(editedFinal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        activityMainBinding.cameraView.setLifecycleOwner(this);

        //take photo button click listener
        activityMainBinding.btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        cypher_title = activityMainBinding.scannedText.getText().toString();


    }


    private Uri saveBitmapToCache(Bitmap bitmap) {
        //get cache directory
        File cachePath = new File(getExternalCacheDir(), "my_images/");
        cachePath.mkdirs();
        File file = new File(cachePath, "Image_123.png");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get file uri
        Uri myImageFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        return myImageFileUri;
    }


    public void processBitmap(Bitmap bitmap, int rotation) {
        InputImage img = InputImage.fromBitmap(bitmap, rotation);
        //taking an instance of google mlkit textrecognizer
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(img).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {

                activityMainBinding.scannedText.setText(text.getText());
                Log.d("mainhai", text.getText());
//
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takePhoto() {
        activityMainBinding.cameraView.takePictureSnapshot();
    }

    private void cropImage(Bitmap bitmap) throws IOException {
        Uri uri = saveBitmapToCache(bitmap);
        Bundle bundle = new Bundle();
        bundle.putString("imageUri", uri.toString());
        Intent i1 = new Intent(MainActivity.this, AfterMainActivity.class);
        i1.putExtras(bundle);
        startActivity(i1);
//        CropImage.activity(uri)
//                .start(this);
    }

//    @Override
//    //crop image activity result listener, this block is executed when the image is cropped
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                Bitmap imageAfterCrop = null;
//                try {
//                    imageAfterCrop = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//
//                    ocr(imageAfterCrop, rotationAfterCrop);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                activityMainBinding.cropImageView.setVisibility(View.VISIBLE);
//                activityMainBinding.cropImageView.setImageBitmap(imageAfterCrop);
//
//            }
//        }
//    }

    //processes bitmap of the cropped image
    private void ocr(Bitmap imageAfterCrop, int rotationAfterCrop) {
        processBitmap(imageAfterCrop, rotationAfterCrop);
    }

    //call the api to recursively decode the cipher
    private void decodeCipher(String text) throws JSONException {

        Intent intent = new Intent(MainActivity.this, DecodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("textToDecode", text);
        if (activityMainBinding.checkBox.isChecked()) {
            bundle.putBoolean("toStore", true);

        } else {
            bundle.putBoolean("toStore", false);

        }
        if (activityMainBinding.radioButton.isChecked()) {
            bundle.putBoolean("method", true);
        }

        if (activityMainBinding.radioButton2.isChecked()) {
            bundle.putBoolean("method", false);

        } else {
            Toast.makeText(this, "Please check at least one method to decode", Toast.LENGTH_SHORT).show();
        }

        intent.putExtras(bundle);
        startActivity(intent);


    }


}