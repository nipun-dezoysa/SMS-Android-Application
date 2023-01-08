package com.example.sms.admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Year;

public class ViewAttendance extends AppCompatActivity {

    LinearLayout grade10, grade11;

    public static String grade;
    public static String month;
    public static String year;


    ImageView view_attendance_back;

    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        grade10 = findViewById(R.id.grade10_atn);
        grade11 = findViewById(R.id.grade11_atn);

        int intYear = Year.now().getValue();
        year = "" + intYear;

        view_attendance_back = findViewById(R.id.view_attendance_back);

        view_attendance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();


        grade10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendance.this, MonthAttendanceList.class);
//                intent.putExtra("grade", 10);
                startActivity(intent);
                grade = "Grade 10";
            }
        });

        grade11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendance.this, MonthAttendanceList.class);
//                intent.putExtra("grade", 11);
                startActivity(intent);
                grade = "Grade 11";
            }
        });
    }
}