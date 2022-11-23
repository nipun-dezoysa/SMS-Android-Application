package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sms.R;
import com.sdsmdg.tastytoast.TastyToast;

public class AttendanceActivity extends AppCompatActivity {

    ImageView attendance_back;
    Button add_attendance;
    Button view_attendance;
//    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendance_back = findViewById(R.id.atn_back);
        add_attendance = findViewById(R.id.add_attendance);
        view_attendance = findViewById(R.id.view_attendance);

//        Intent intent= getIntent();
//        uname = intent.getStringExtra("uname");

        attendance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                finish();
            }
        });

        add_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this,Add_Attendance.class);
                startActivity(intent);
            }
        });

        view_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(AttendanceActivity.this, "under construction", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);


            }
        });
    }
}