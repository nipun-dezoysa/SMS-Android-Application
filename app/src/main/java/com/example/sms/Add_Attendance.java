package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.adapter.Attendance_Adapter;
import com.example.sms.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Add_Attendance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Student> list;
    DatabaseReference databaseReference;
    Attendance_Adapter attendanceAdapter;

    TextView attendance_date;

    ImageView button_back;
    FloatingActionButton save;

    TextView grade10;
    TextView grade11;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        button_back = findViewById(R.id.add_attendance_back);
        save = findViewById(R.id.save_student_attendance);

        grade10 = findViewById(R.id.grade10_list);
        grade11 = findViewById(R.id.grade11_list);

        attendance_date = findViewById(R.id.attendance_date);

        attendance_date.setText(getCurrentDate());

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                TastyToast.makeText(Add_Attendance.this, " Under Construction!", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });

        recyclerView = findViewById(R.id.stud_attendance_recyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceAdapter = new Attendance_Adapter(this, list);
        recyclerView.setAdapter(attendanceAdapter);


        grade10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Student student = dataSnapshot.getValue(Student.class);
                            if (student.getGrade() == 10) {
                                String uname = dataSnapshot.getKey();
                                student.setUsername(uname);
                                list.add(student);
                            }
                        }
                        attendanceAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        grade11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Student student = dataSnapshot.getValue(Student.class);
                            if (student.getGrade() == 11) {
                                String uname = dataSnapshot.getKey();
                                student.setUsername(uname);
                                list.add(student);
                            }
                        }
                        attendanceAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
    private String getCurrentDate() {

        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

}