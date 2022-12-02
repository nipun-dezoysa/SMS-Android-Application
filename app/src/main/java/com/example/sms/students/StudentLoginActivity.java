package com.example.sms.students;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sms.MainActivity;
import com.example.sms.R;
import com.example.sms.admin.TeacherPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.paperdb.Paper;

public class StudentLoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    String outputPassword;
    String encryptedPassword;
    String AES = "AES";

    ProgressBar progressBar;

    EditText username;
    EditText password;
    Button btnStdLogin;
    TextView button1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlogin);

        username = findViewById(R.id.txtStdUserName);
        password = findViewById(R.id.txtStdPassword);
        btnStdLogin = findViewById(R.id.btnStdLogin);
        button1 = findViewById(R.id.btnTxtStdLogin);
        progressBar = findViewById(R.id.progressBarStudLogin);

        Paper.init(StudentLoginActivity.this);
        String UserNameKey = Paper.book().read(OnlineUsers.UserNamekey);
        String UserPasswordKey = Paper.book().read(OnlineUsers.UserPasswordKey);

        if (UserNameKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                autoLogin(UserNameKey, UserPasswordKey);
            }
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(StudentLoginActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        });

        btnStdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pword = password.getText().toString();

                if(uname.equals("") || pword.equals(""))
                {
                    TastyToast.makeText(StudentLoginActivity.this, "Username or Password is empty", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }else{
                    try {
                        outputPassword = encrypt(pword, pword);
                        encryptedPassword = outputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginUser(uname,encryptedPassword);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void autoLogin(String userNameKey, String userPasswordKey) {

        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userNameKey)){
                    String dbPassword = snapshot.child(userNameKey).child("password").getValue(String.class);

                    if(dbPassword.equals(userPasswordKey)){
//                        TastyToast.makeText(StudentLoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent studentLoginIntent = new Intent(StudentLoginActivity.this, StudentFingerPrintAuth.class);
                        studentLoginIntent.putExtra("uname", userNameKey);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(studentLoginIntent);


                    } else{
                        TastyToast.makeText(StudentLoginActivity.this, "It seems you have changed your password. Please login with your new password.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else {
                    TastyToast.makeText(StudentLoginActivity.this, "Please visit your relevant login page.", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(StudentLoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private String  encrypt(String data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    private void loginUser(String uname, String pword){
        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uname)){
                    String dbPassword = snapshot.child(uname).child("password").getValue(String.class);

                    if(dbPassword.equals(pword)){
                        TastyToast.makeText(StudentLoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent studentLoginIntent = new Intent(StudentLoginActivity.this, StudentPageActivity.class);
                        Paper.book().write(OnlineUsers.UserNamekey, uname);
                        Paper.book().write(OnlineUsers.UserPasswordKey, pword);
                        studentLoginIntent.putExtra("uname", uname);
                        startActivity(studentLoginIntent);


                    } else{
                        TastyToast.makeText(StudentLoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } else{
                    TastyToast.makeText(StudentLoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(StudentLoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
