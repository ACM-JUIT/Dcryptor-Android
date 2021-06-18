package com.anshagrawal.dcmlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    int rotationAfterCrop;
    ActivityMainBinding activityMainBinding;
    String url = "https://acm-dcryptor.herokuapp.com/api/v1/";
    String absolutePath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);



        //camera listener, listens the activity of camera
        activityMainBinding.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                rotationAfterCrop = result.getRotation();
                result.toBitmap(new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        Log.i("bla", Integer.toString(result.getRotation()));
                        try {
                            saveBitmapToCache(bitmap);
                            cropImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        processBitmap(bitmap, result.getRotation());
                    }
                });
            }
        });

        activityMainBinding.cameraView.setLifecycleOwner(this);

        //take photo button click listener
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

    //Saves the bitmap to external cache directory so that the image does not get stored in the device
    // storage and return Uri for the image cropping library

    private Uri saveBitmapToCache(Bitmap bitmap) {
        //get cache directory
        File cachePath = new File(getExternalCacheDir(), "my_images/");
        cachePath.mkdirs();
        File file = new File(cachePath, "Image_123.png");
        FileOutputStream fileOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        //get file uri
        Uri myImageFileUri =  FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        return myImageFileUri;
    }


    private int getImageRotation(int rotation) {
        return rotation;
    }

    //convert bitmap to uri which is required for crop image library
    public Uri getImageUri(Context inContext, Bitmap inImage) throws IOException {
        String filename = "ii";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, filename, "blehhh");
        Log.i("wah", path);
        absolutePath = path;
        bytes.close();

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

    private void cropImage(Bitmap bitmap) throws IOException {
//        Uri uri = getImageUri(this, bitmap);
            Uri uri = saveBitmapToCache(bitmap);
//        File file = new File(uri.getPath());
        Log.i("kl", uri.getPath());
        CropImage.activity(uri)
                .start(this);


    }

    private void deleteImageAfterGettingUri(Uri uri) {
//        Log.i("hg", getRealPathFromURI(MainActivity.this, absolutePath));
//        File del= new File(getPath);
//        Log.i("tag", del.exists()+"");
//        Log.i("tag", del.getAbsolutePath()+"");
//        Log.i("tag", del.getPath()+"");
//        Log.i("tag", del.isDirectory()+"");
//        Log.i("tag", del.isFile()+"");
//        Log.i("tag", del.delete()+"");


        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ absolutePath});


//        if(del.exists()){
//            Log.i("hg", "file exists, attempting to delete");
//            if(del.delete()){
//                Log.i("hg", "del");
//            }
//            else{
//                Log.i("hg", "no del");
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

//                Log.i("vcvc", getRealPathFromURI(MainActivity.this, absolutePath));
//                deleteImageAfterGettingUri(absolutePath);
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
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void ocr(Bitmap imageAfterCrop, int rotationAfterCrop) {
        processBitmap(imageAfterCrop, rotationAfterCrop);
//        deleteImageAfterGettingUri(absolutePath);
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

                    String title = jsonArray.getString(jsonArray.length()-1);
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
        Log.i("hg", "decodeCipher: ");
    }
}