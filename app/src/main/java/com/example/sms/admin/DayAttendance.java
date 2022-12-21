package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.sms.R;
import com.example.sms.adapter.ViewDayAttendanceAdapter;
import com.example.sms.adapter.ViewMonthAttendance_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DayAttendance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> list;
    ViewDayAttendanceAdapter viewDayAttendance_adapter;
    DatabaseReference databaseReference;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_attendance);

        intent = getIntent();

        String month = intent.getStringExtra("month");
        String grade = ViewAttendance.grade;

        recyclerView = findViewById(R.id.day_attendance_recyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child("2022").child(month);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewDayAttendance_adapter = new ViewDayAttendanceAdapter(this, list);
        recyclerView.setAdapter(viewDayAttendance_adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.hasChild(grade))
                        list.add(dataSnapshot.getKey());
                }
                viewDayAttendance_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}