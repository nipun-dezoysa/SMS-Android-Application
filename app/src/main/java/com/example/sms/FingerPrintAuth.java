package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Executable;
import java.util.concurrent.Executor;

public class FingerPrintAuth extends AppCompatActivity {

    androidx.biometric.BiometricPrompt biometricPrompt;
    androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_auth);


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                TastyToast.makeText(this, "Your device doesn't have fingerprint", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                TastyToast.makeText(this, "Your device fingerprint is not working", TastyToast.LENGTH_SHORT, TastyToast.INFO);


            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                TastyToast.makeText(this, "No fingerprint assigned", TastyToast.LENGTH_SHORT, TastyToast.INFO);

        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new androidx.biometric.BiometricPrompt(FingerPrintAuth.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                TastyToast.makeText(getApplicationContext(), "Logging in", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                Intent intent = new Intent(FingerPrintAuth.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder().setTitle("ASMS")
                .setDescription("Use fingerprint to login").setDeviceCredentialAllowed(true).build();

        biometricPrompt.authenticate(promptInfo);
    }
}