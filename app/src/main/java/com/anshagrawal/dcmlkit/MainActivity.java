package com.anshagrawal.dcmlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anshagrawal.dcmlkit.databinding.ActivityMainBinding;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    int rotationAfterCrop;
    ActivityMainBinding activityMainBinding;
    String url = "https://acm-dcryptor.herokuapp.com/api/v1/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);


        activityMainBinding.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                rotationAfterCrop = result.getRotation();
                result.toBitmap(new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        Log.i("bla", Integer.toString(result.getRotation()));
                        cropImage(bitmap);
//                        processBitmap(bitmap, result.getRotation());
                    }
                });
            }
        });

        activityMainBinding.cameraView.setLifecycleOwner(this);

        activityMainBinding.btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.scannedText.setVisibility(View.VISIBLE);
                activityMainBinding.decodedText.setVisibility(View.GONE);
                takePhoto();
            }
        });

//        activityMainBinding.btnDecode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activityMainBinding.scannedText.setVisibility(View.GONE);
//                activityMainBinding.decodedText.setVisibility(View.VISIBLE);
//            }
//        });
    }

    private int getImageRotation(int rotation) {
        return rotation;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "yourimage", null);
        return Uri.parse(path);
    }

    private void processBitmap(Bitmap bitmap, int rot) {
        InputImage img = InputImage.fromBitmap(bitmap, rot);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(img).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                Log.i("bla", text.getText());
//                Toast.makeText(MainActivity.this, text.getText(), Toast.LENGTH_SHORT).show();
                activityMainBinding.scannedText.setText(text.getText());
                activityMainBinding.btnDecode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editedOrFinal = activityMainBinding.scannedText.getText().toString();
                        activityMainBinding.scannedText.setVisibility(View.GONE);
                        activityMainBinding.decodedText.setVisibility(View.VISIBLE);

                        try {
                            decodeCipher(editedOrFinal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("bla", e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void takePhoto() {
        activityMainBinding.cameraView.takePictureSnapshot();

    }

    private void cropImage(Bitmap bitmap) {
        Uri uri = getImageUri(this, bitmap);
        CropImage.activity(uri)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap imageAfterCrop = null;
                try {
                    imageAfterCrop = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ocr(imageAfterCrop, rotationAfterCrop);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activityMainBinding.cropImageView.setImageBitmap(imageAfterCrop);
            }
        }
    }

    private void ocr(Bitmap imageAfterCrop, int rotationAfterCrop) {
        processBitmap(imageAfterCrop, rotationAfterCrop);
    }

    private void decodeCipher(String text) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("data", text);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("decoded_data");
                    String finalDecodedText ="";
                    for(int i=0;i<jsonArray.length();i++){
                        finalDecodedText+=jsonArray.getString(i)+"\n";
                    }
                    activityMainBinding.decodedText.setText(finalDecodedText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activityMainBinding.decodedText.setText("error");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}