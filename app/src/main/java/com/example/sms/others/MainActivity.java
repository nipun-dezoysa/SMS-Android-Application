package com.example.sms.others;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sms.R;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);

//        Button btn_student = (Button) findViewById(R.id.student);
//        Button btn_teacher = (Button) findViewById(R.id.teacher);

//        btn_student.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent studentLoginIntent = new Intent(MainActivity.this, StudentLoginActivity.class);
//                startActivity(studentLoginIntent);
//                finish();
//            }
//        });
//
//        btn_teacher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent teacherLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(teacherLoginIntent);
//                finish();
//            }
//        });

    }

}