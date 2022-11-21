package com.example.sms.admin;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sms.MainActivity;
import com.example.sms.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;


public class TeacherLoginActivity extends AppCompatActivity {

    //DatabaseReference databaseReference;

    ProgressBar progressBarOfTeacherLogin;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherlogin);

        EditText username = (EditText) findViewById(R.id.txtTeaUserName);
        TextView password = (TextView) findViewById(R.id.txtTeaPassWord);
        Button login_btn = (Button) findViewById(R.id.btnTeaLogin);
        TextView back_btn = (TextView) findViewById(R.id.btntxtTeaLogin);
        progressBarOfTeacherLogin = (ProgressBar) findViewById(R.id.progressBarOfTeacherLogin);

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
                 loginUser(uname,pword);
                 progressBarOfTeacherLogin.setVisibility(View.VISIBLE);
//                 finish();
             }
            }

        });

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
                        teacherLoginIntent.putExtra("uname",uname);
                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                        startActivity(teacherLoginIntent);

                    } else{
                        TastyToast.makeText(TeacherLoginActivity.this, "Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                        progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                    }
                }else{
                    TastyToast.makeText(TeacherLoginActivity.this, "User does not exist in the system", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
