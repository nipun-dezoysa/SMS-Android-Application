package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.adapter.ViewDayAttendanceAdapter;
import com.example.sms.adapter.ViewMonthAttendance_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DayAttendance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> list;
    ViewDayAttendanceAdapter viewDayAttendance_adapter;
    DatabaseReference databaseReference;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    Intent intent;
    ImageView day_atn_back;
    TextView setGrade,setMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_attendance);

        intent = getIntent();

        String year = ViewAttendance.year;
        String month = intent.getStringExtra("month");
        String grade = ViewAttendance.grade;

        setGrade = findViewById(R.id.setGrade1);
        setMonth = findViewById(R.id.month);

        setGrade.setText(grade);

        int m = Integer.parseInt(month);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        cal.set(Calendar.MONTH, m-1);
        String month_name = month_date.format(cal.getTime());

        Log.e("",""+month_name);

        setMonth.setText(month_name);


        day_atn_back = findViewById(R.id.day_atn_back);

        day_atn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.day_attendance_recyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child(year).child(month);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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