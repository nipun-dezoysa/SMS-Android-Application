package com.example.sms.admin;

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
import com.example.sms.students.OnlineUsers;
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


public class TeacherLoginActivity extends AppCompatActivity {

    //DatabaseReference databaseReference;

    String outputPassword;
    String encryptedPassword;
    String AES = "AES";

    ProgressBar progressBarOfTeacherLogin;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherlogin);

        EditText username = findViewById(R.id.txtTeaUserName);
        TextView password = findViewById(R.id.txtTeaPassWord);
        Button login_btn = findViewById(R.id.btnTeaLogin);
        TextView back_btn = findViewById(R.id.btntxtTeaLogin);
        progressBarOfTeacherLogin = findViewById(R.id.progressBarOfTeacherLogin);

        Paper.init(TeacherLoginActivity.this);
        String UserNameKey = Paper.book().read(OnlineUsers.UserNamekey);
        String UserPasswordKey = Paper.book().read(OnlineUsers.UserPasswordKey);

        if (UserNameKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                autoLogin(UserNameKey, UserPasswordKey);
            }
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(TeacherLoginActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        });
        
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String uname = username.getText().toString();
             String pword = password.getText().toString();

             if(uname.equals("") || pword.equals(""))
             {
                 TastyToast.makeText(v.getContext(), "Username or Password is empty", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

             }else{
                 try {
                     outputPassword = encrypt(pword, pword);
                     encryptedPassword = outputPassword;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 loginUser(uname,encryptedPassword);
                 progressBarOfTeacherLogin.setVisibility(View.VISIBLE);
//                 finish();
             }
            }

        });

    }

    private void autoLogin(String userNameKey, String userPasswordKey) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userNameKey)){
                    String dbPassword = snapshot.child(userNameKey).child("password").getValue(String.class);

                    if(dbPassword.equals(userPasswordKey)){
                        TastyToast.makeText(TeacherLoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent loginIntent = new Intent(TeacherLoginActivity.this, TeacherPageActivity.class);
                        loginIntent.putExtra("uname", userNameKey);
                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                        startActivity(loginIntent);



                    } else{
                        TastyToast.makeText(TeacherLoginActivity.this, "It seems you have changed your password. Please login with your new password.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                    }
                }else {
                    TastyToast.makeText(TeacherLoginActivity.this, "Please visit your relevant login page.", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                    progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(TeacherLoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
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
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uname)){
                    String dbPassword = snapshot.child(uname).child("password").getValue(String.class);

                    if(dbPassword.equals(pword)){
                        TastyToast.makeText(TeacherLoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent teacherLoginIntent = new Intent(TeacherLoginActivity.this, TeacherPageActivity.class);
                        Paper.book().write(OnlineUsers.UserNamekey, uname);
                        Paper.book().write(OnlineUsers.UserPasswordKey, pword);
                        teacherLoginIntent.putExtra("uname",uname);
                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                        startActivity(teacherLoginIntent);


                    } else{
                        TastyToast.makeText(TeacherLoginActivity.this, "Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                    }
                }else{
                    TastyToast.makeText(TeacherLoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(TeacherLoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
            }
        });
    }

}
