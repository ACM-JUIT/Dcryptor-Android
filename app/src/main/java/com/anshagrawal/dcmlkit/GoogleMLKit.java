package com.anshagrawal.dcmlkit;

import android.graphics.Bitmap;
import android.net.wifi.aware.IdentityChangedListener;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
public class GoogleMLKit {

    @NonNull
    String text1;
    public String processBitmap(Bitmap bitmap, int rotation) {
        InputImage img = InputImage.fromBitmap(bitmap, rotation);
        //taking an instance of google mlkit textrecognizer
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(img).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                text1 = text.getText();

                Log.d("hhhh", "Success " + text1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                text1 = e.toString();
                Log.d("hhhh", "Success " + text1);
            }
        });
        Log.d("hhhh", "returning " + text1);

       return text1;
}}
