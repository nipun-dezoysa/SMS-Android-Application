package com.example.sms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_student = (Button) findViewById(R.id.student);
        Button btn_teacher = (Button) findViewById(R.id.teacher);

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentLoginIntent = new Intent(MainActivity.this,StudentLoginActivity.class);
                startActivity(studentLoginIntent);
                finish();
            }
        });

        btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacherLoginIntent = new Intent(MainActivity.this,TeacherLoginActivity.class);
                startActivity(teacherLoginIntent);
                finish();
            }
        });

    }

}