package com.anshagrawal.dcmlkit.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anshagrawal.dcmlkit.Adapters.CypherAdapter;
import com.anshagrawal.dcmlkit.BuildConfig;
import com.anshagrawal.dcmlkit.GoogleMLKit;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.MySingleton;
import com.anshagrawal.dcmlkit.R;
import com.anshagrawal.dcmlkit.ViewModel.CypherViewModel;
import com.anshagrawal.dcmlkit.databinding.ActivityMainBinding;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int rotationAfterCrop;
    ActivityMainBinding activityMainBinding;
    String url = BuildConfig.LINK;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> grocery;
    String cypher_title;
    CypherAdapter adapter;
    CypherViewModel cypherViewModel;
    String sTitle;

    GoogleMLKit googleMLKit = new GoogleMLKit();

//    private SharedPreferencesClass sharedPreferences;
//    private SharedPreferencesClass.Editor editor;
//    private int checkedItem;
//    private String selected;


    ArrayList<String> decodes;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);


        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);


        sTitle = getIntent().getStringExtra("title");

        activityMainBinding.scannedText.setText(sTitle);
//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivity(intent);
        dialog = new ProgressDialog(this);
        dialog.setMessage("We are processing your result. \n Please Wait...");
        dialog.setCancelable(false);
        //for dark mode
//        sharedPreferences = this.getSharedPreferences("themes", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();


//        real time text detector
//        activityMainBinding.cameraView.addFrameProcessor(new FrameProcessor() {
//            @Override
//            public void process(@NonNull Frame frame) {
//                processImage(getInputImageFromFrame(frame));
//            }
//        });
        //for dark mode
//        switch (getCheckedItem()) {
//            case 0:
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//                break;
//
//            case 1:
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                break;
//
//            case 2:
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                break;
//        }


        decodes = new ArrayList<>();


        activityMainBinding.cameraView.setBackgroundResource(R.drawable.rounded_corner);
        activityMainBinding.cameraView.setClipToOutline(true);

        activityMainBinding.scannedText.setBackgroundResource(R.drawable.rounded_corner);
        activityMainBinding.scannedText.setClipToOutline(true);

//        activityMainBinding.myListView.setBackgroundResource(R.drawable.rounded_corner);
//        activityMainBinding.myListView.setClipToOutline(true);

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
//                        processBitmap(bitmap, result.getRotation());
                    }
                });
            }
        });

//        activityMainBinding.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String text = ((TextView) view).getText().toString();
//                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                ClipData clipData = ClipData.newPlainText("Copied to clipboard!", text);
//                clipboardManager.setPrimaryClip(clipData);
//                Toast.makeText(MainActivity.this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
//            }
//        });
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

//                cypher_title = activityMainBinding.scannedText.getText().toString();
//                CreateCypher(cypher_title);
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
        cypherViewModel = ViewModelProviders.of(this).get(CypherViewModel.class);

        cypher_title = activityMainBinding.scannedText.getText().toString();

//        activityMainBinding.btnaddtoDashboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cypher_title = activityMainBinding.scannedText.getText().toString();
//                CreateCypher(cypher_title);
//
//
//            }
//        });


    }

    public void CreateCypher(String cypher_title) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMM d, yyyy", date.getTime());
//        //CharSequence sequence1 = Timestamp.valueOf(date.getTime());
//
//        Timestamp sequence1 = new Timestamp(date.getTime());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String currentTime = simpleDateFormat.format(calendar.getTime());


        String result = null;
        try {
            Date date1 = simpleDateFormat.parse(currentTime);
            calendar.setTime(date1);
            calendar.add(Calendar.MINUTE, 15);
            result = simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Dcryptor dcryptor1 = new Dcryptor();
        dcryptor1.cypherTitle = cypher_title;
        dcryptor1.cypherDate = sequence.toString();
        dcryptor1.cypherTime = currentTime;

        cypherViewModel.insertCypher(dcryptor1);


//        startActivity(new Intent(MainActivity.this, DecodeActivity.class));

        finish();

    }

//    real time text detection


//    private void processImage(InputImage inputImageFromFrame) {
//
//        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//        textRecognizer.process(inputImageFromFrame).addOnSuccessListener(new OnSuccessListener<Text>() {
//            @Override
//            public void onSuccess(@NonNull Text text) {
//                activityMainBinding.scannedText.setText("");
//                activityMainBinding.scannedText.setText(text.getText());
//                for (Text.TextBlock block : text.getTextBlocks()) {
//                    String blockText = block.getText();
//                    Point[] blockCornerPoints = block.getCornerPoints();
//                    Rect blockFrame = block.getBoundingBox();
//                    for (Text.Line line : block.getLines()) {
//                        String lineText = line.getText();
//                        Point[] lineCornerPoints = line.getCornerPoints();
//                        Rect lineFrame = line.getBoundingBox();
//                        for (Text.Element element : line.getElements()) {
//                            String elementText = element.getText();
//                            Point[] elementCornerPoints = element.getCornerPoints();
//                            Rect elementFrame = element.getBoundingBox();
//                        }
//                    }
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("poop", "onFailure: ");
//            }
//        });
//    }


    //    real time text detector
//    private InputImage getInputImageFromFrame(Frame frame) {
//        byte[] data = frame.getData();
//        return InputImage.fromByteArray(data, frame.getSize().getWidth(), frame.getSize().getHeight(), frame.getRotation(), frame.getFormat());
//    }


    //Saves the bitmap to external cache directory so that the image does not get stored in the device
    // storage and return Uri for the image cropping library
//realtime


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
        CropImage.activity(uri)
                .start(this);
    }

    @Override
    //crop image activity result listener, this block is executed when the image is cropped
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
                activityMainBinding.cropImageView.setVisibility(View.VISIBLE);
                activityMainBinding.cropImageView.setImageBitmap(imageAfterCrop);

            }
        }
    }

    //processes bitmap of the cropped image
    private void ocr(Bitmap imageAfterCrop, int rotationAfterCrop) {
        processBitmap(imageAfterCrop, rotationAfterCrop);
    }

    //call the api to recursively decode the cipher
    private void decodeCipher(String text) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            dialog.show();
//            jsonObject.put("data", text);
//        } catch (JSONException e) {
//            dialog.dismiss();
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    dialog.show();
//                    JSONArray jsonArray = response.getJSONArray("decoded_data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String s = jsonArray.getString(i);
//                        decodes.add(s);
//                    }
//                    String[] strArray = new String[decodes.size()];
//                    for (int i = 0; i < decodes.size(); i++) {
//                        strArray[i] = decodes.get(i);
//                    }
//                    Intent intent = new Intent(MainActivity.this, DecodeActivity.class);
//                    Bundle bundle = new Bundle();
////                    bundle.putStringArray("decodedTextStringArray", strArray);
//                    bundle.putString("textToDecode", text);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    CreateCypher(text);
//                    dialog.dismiss();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        CreateCypher(text);
        Intent intent = new Intent(MainActivity.this, DecodeActivity.class);
        Bundle bundle = new Bundle();
//                    bundle.putStringArray("decodedTextStringArray", strArray);
        bundle.putString("textToDecode", text);
        intent.putExtras(bundle);
        startActivity(intent);

    }


}