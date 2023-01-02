package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;

public class ReportActivity extends AppCompatActivity {

    ImageView report_back;
    LinearLayout studListReport;
    LinearLayout examResults;
    LinearLayout studentContacts;
    LinearLayout attendanceReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        studListReport = findViewById(R.id.studListReport);
        examResults = findViewById(R.id.examResults);
        report_back = findViewById(R.id.report_back);
        studentContacts = findViewById(R.id.studentContacts);
        attendanceReport = findViewById(R.id.attendanceReport);



        report_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        examResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this, ExamsResultsReport.class);
                startActivity(i);
            }
        });


    }
}