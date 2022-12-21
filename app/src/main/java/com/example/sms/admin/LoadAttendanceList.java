package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.adapter.ViewLoadAttendanceAdapter;
import com.example.sms.adapter.ViewMonthAttendance_Adapter;
import com.example.sms.model.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadAttendanceList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Attendance> list;
    ViewLoadAttendanceAdapter viewLoadAttendance_adapter;
    DatabaseReference databaseReference;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    TextView finalAttendanceDate;
    ImageView loadAttendance_back;

    Intent intent;
    TextView setGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_attendance_list);

        setGrade = findViewById(R.id.setGrade2);
        setGrade.setText(ViewAttendance.grade);
        loadAttendance_back = findViewById(R.id.loadAttendance_back);
        loadAttendance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intent = getIntent();
        String year = ViewAttendance.year;
        String month = ViewAttendance.month;
        String dmonth = ViewAttendance.month;
        if(Integer.parseInt(month)<10)
            dmonth="0"+dmonth;

        String grade = ViewAttendance.grade;
        String day = intent.getStringExtra("day");
        String dday = day;
        if (Integer.parseInt(day)<10)
            dday = "0"+dday;

        finalAttendanceDate = findViewById(R.id.finalAttendanceDate);

        finalAttendanceDate.setText("2022-"+ dmonth + "-"+dday);

        recyclerView = findViewById(R.id.load_attendance_rv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child(year).child(month).child(day).child(grade);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewLoadAttendance_adapter = new ViewLoadAttendanceAdapter(this, list);
        recyclerView.setAdapter(viewLoadAttendance_adapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    list.add(attendance);
                }
                viewLoadAttendance_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}