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

import com.example.sms.R;
import com.example.sms.adapter.ViewMonthAttendance_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MonthAttendanceList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String>list;
    ViewMonthAttendance_Adapter viewMonthAttendance_adapter;
    DatabaseReference databaseReference;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    ImageView monthAttendance_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_attendance_list);

        String year = ViewAttendance.year;

        monthAttendance_back = findViewById(R.id.monthAttendance_back);

        monthAttendance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        recyclerView = findViewById(R.id.monthAttendance_recyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child(year);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewMonthAttendance_adapter = new ViewMonthAttendance_Adapter(this, list);
        recyclerView.setAdapter(viewMonthAttendance_adapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
                viewMonthAttendance_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}