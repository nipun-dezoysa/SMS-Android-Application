package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.example.sms.R;
import com.example.sms.students.OnlineUsers;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.concurrent.Executor;

import io.paperdb.Paper;

public class AdminFingerPrintAuth extends AppCompatActivity {

    String uname;

    androidx.biometric.BiometricPrompt biometricPrompt;
    androidx.biometric.BiometricPrompt.PromptInfo promptInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Intent intent= getIntent();
//        uname = intent.getStringExtra("uname");

        Paper.init(AdminFingerPrintAuth.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                TastyToast.makeText(this, "Your device doesn't have fingerprint", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                TastyToast.makeText(this, "Your device fingerprint is not working", TastyToast.LENGTH_SHORT, TastyToast.INFO);


            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                TastyToast.makeText(this, "No fingerprint assigned", TastyToast.LENGTH_SHORT, TastyToast.INFO);

                Intent intent1 = new Intent(AdminFingerPrintAuth.this, TeacherPageActivity.class);
//                intent.putExtra("uname", uname);
                startActivity(intent1);
                finish();
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new androidx.biometric.BiometricPrompt(AdminFingerPrintAuth.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                TastyToast.makeText(getApplicationContext(), "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent = new Intent(AdminFingerPrintAuth.this, TeacherPageActivity.class);
//                intent.putExtra("uname", uname);
                startActivity(intent);
                finish();
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