package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;

public class ReportActivity extends AppCompatActivity {

    LinearLayout studListReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        studListReport =findViewById(R.id.studListReport);
        ImageView report_back = (ImageView) findViewById(R.id.report_back);
        report_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        studListReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this,StudentListReport.class);
                startActivity(i);
            }
        });
    }
}