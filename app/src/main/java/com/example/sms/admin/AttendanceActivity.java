package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;
import com.example.sms.students.OnlineUsers;
import com.sdsmdg.tastytoast.TastyToast;

import io.paperdb.Paper;

public class AttendanceActivity extends AppCompatActivity {

    ImageView attendance_back;
    LinearLayout add_attendance;
    LinearLayout view_attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendance_back = findViewById(R.id.atn_back);
        add_attendance = findViewById(R.id.add_attendance);
        view_attendance = findViewById(R.id.view_attendance);



        attendance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

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