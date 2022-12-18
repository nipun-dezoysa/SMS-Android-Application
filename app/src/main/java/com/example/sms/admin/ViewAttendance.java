package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sms.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewAttendance extends AppCompatActivity {

    LinearLayout grade10, grade11;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        grade10 = findViewById(R.id.grade10_atn);
        grade11 = findViewById(R.id.grade11_atn);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        grade10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendance.this,LoadAttendanceList.class);
                intent.putExtra("grade", 10);
                startActivity(intent);
            }
        });

        grade11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendance.this, LoadAttendanceList.class);
                intent.putExtra("grade", 11);
                startActivity(intent);
            }
        });
    }
}