package com.anshagrawal.dcmlkit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.anshagrawal.dcmlkit.databinding.ActivitySplashBinding;

import java.util.concurrent.Executor;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        getSupportActionBar().hide();
        activitySplashBinding.textViewY.animate().rotation(360).setDuration(800);

        //launch main activity after 3.2s os splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                finish();
            }
        }, 1000);
        checkForFingerprint();
        //Fingerprint image added when the user clicks the image a prompt will open and when the user will authenticate then only the user will be allowed to enter in the app.
//        activitySplashBinding.fingerprint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                        .setTitle("Please Verify")
//                        .setDescription("User Authentication is required to proceed")
//                        .setNegativeButtonText("Cancel")
//                        .build();
//
//                getPrompt().authenticate(promptInfo);
//            }
//        });
    }

    void checkForFingerprint(){
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Please Verify")
                .setDescription("User Authentication is required to proceed")
                .setNegativeButtonText("Cancel")
                .build();

        getPrompt().authenticate(promptInfo);
    }


    //For security of the app by executing the biometric.
    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("Authentication Succeeded!!!!");
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("Authentication Failed!!!!!");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }


    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}