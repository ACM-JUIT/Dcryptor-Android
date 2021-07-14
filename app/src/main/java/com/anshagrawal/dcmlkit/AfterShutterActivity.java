//java
package com.anshagrawal.dcmlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.anshagrawal.dcmlkit.Activities.DecodeActivity;
import com.anshagrawal.dcmlkit.databinding.ActivityAfterShutterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

 public class AfterShutterActivity extends AppCompatActivity {
    Uri imageUri;
    ActivityAfterShutterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterShutterBinding.inflate(getLayoutInflater());
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
                decode();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ggg", e.getMessage());
            }
        });
    }

    private void decode() {
        binding.decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                final boolean[] toStore = new boolean[1];
                final boolean[] method = new boolean[1];
                Dialog dialog1 = new Dialog(AfterShutterActivity.this);
                dialog1.setContentView(R.layout.dialog_resource);
                Button doneBtn = (Button) dialog1.findViewById(R.id.doneBtn);
                CheckBox checkBox = (CheckBox) dialog1.findViewById(R.id.checkBox);
                RadioButton radioButton1 = (RadioButton) dialog1.findViewById(R.id.radioButton);
                RadioButton radioButton2 = (RadioButton) dialog1.findViewById(R.id.radioButton2);
                if (checkBox.isChecked()) {
                    toStore[0] = true;

                } else {
                    toStore[0] = false;

                }
                if (radioButton1.isChecked()) {
                    method[0] = true;
                }

                if (radioButton2.isChecked()) {
                    method[0] = true;

                } else {
//                    Toast.makeText(, "Please check at least one method to decode", Toast.LENGTH_SHORT).show();
                }

                //
                dialog1.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textToDecode = binding.editText.getText().toString();
                        Intent intent = new Intent(AfterShutterActivity.this, DecodeActivity.class);
                        finish();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("textToDecode", textToDecode);
                        bundle1.putBoolean("toStore", toStore[0]);
                        bundle1.putBoolean("method", method[0]);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                    }
                });
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