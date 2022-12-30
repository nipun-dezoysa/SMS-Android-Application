package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.adapter.Attendance_Adapter;
import com.example.sms.model.Attendance;
import com.example.sms.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Add_Attendance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Student> list;
    public static List<Attendance> attendanceList = new ArrayList<>();
    DatabaseReference databaseReference;
    Attendance_Adapter attendanceAdapter;
    DatePickerDialog.OnDateSetListener setListener;


    public static TextView attendance_date;
    ImageView button_back;
    public static FloatingActionButton save;

    public static String grade;

    TextView grade10;
    TextView grade11;
    int dateOfMonth, monthOfYear, yearOfYear;

    TextView monthName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);


        button_back = findViewById(R.id.add_attendance_back);
        save = findViewById(R.id.save_student_attendance);

        grade10 = findViewById(R.id.grade10_list);
        grade11 = findViewById(R.id.grade11_list);

        attendance_date = findViewById(R.id.attendance_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        attendance_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Add_Attendance.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = year+"/"+month+"/"+day;
//                dateOfMonth = day;
//                monthOfYear = month;
//                yearOfYear = year;
                attendance_date.setText(date);

//                Calendar cal=Calendar.getInstance();
//                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
//                cal.set(Calendar.MONTH, monthOfYear-1);
//                String month_name = month_date.format(cal.getTime());
//
//                Log.e("",""+month_name);
//                monthName.setText(month_name);

            }
        };

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                grade10.setTextColor(getResources().getColor(R.color.white));
                grade10.setBackground(getDrawable(R.drawable.switch_trcks));
                grade11.setBackground(null);
                grade11.setTextColor(getResources().getColor(R.color.darkgreen));

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
                                attendanceList.clear();
                            }
                        }
                        attendanceAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                grade = "Grade 10";
            }

        });

        grade11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade11.setTextColor(getResources().getColor(R.color.white));
                grade11.setBackground(getDrawable(R.drawable.switch_trcks));
                grade10.setBackground(null);
                grade10.setTextColor(getResources().getColor(R.color.darkgreen));

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
                                attendanceList.clear();
                            }
                        }
                        attendanceAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                grade = "Grade 11";
            }
        });
    }


//    private String getCurrentDate() {
//
//        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//    }

}