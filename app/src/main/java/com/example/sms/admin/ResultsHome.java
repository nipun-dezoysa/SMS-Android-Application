package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;

public class ResultsHome extends AppCompatActivity {

    LinearLayout term1,term2,term3;

    public static String term;

    ImageView term_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_home);

        term1 = findViewById(R.id.term1);
        term2 = findViewById(R.id.term2);
        term3 = findViewById(R.id.term3);
        term_back = findViewById(R.id.term_back);

        term_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        term1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsHome.this, ExamsResults.class);
                term = "Term 1";
                startActivity(intent);
            }
        });

        term2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsHome.this, ExamsResults.class);
                term = "Term 2";
                startActivity(intent);
            }
        });

        term3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsHome.this, ExamsResults.class);
                term = "Term 3";
                startActivity(intent);
            }
        });
    }
}