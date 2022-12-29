package com.example.sms.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ScheduleStud extends AppCompatActivity {

    ImageView schedule_stud_back;
    ImageView schedule_back;
    EditText mondayMaths,mondayScience,tuesdayMaths,tuesdayScience,wednesdayMaths,wednesdayScience,thursdayMaths,thursdayScience,fridayMaths,fridayScience,saturdayMaths,saturdayScience,sundayMaths,sundayScience;
    TextView grade10_schedule,grade11_schedule;
    FloatingActionButton saveSchedule;
    List<String> mathsList;
    List<String> scienceList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;

    int getgrade;
    String grade;

    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        mathsList = new ArrayList<>();
        scienceList = new ArrayList<>();

        Paper.init(ScheduleStud.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);

        schedule_back = findViewById(R.id.schedule_back);
        saveSchedule = findViewById(R.id.saveSchedule);
        mondayMaths = findViewById(R.id.mondayMaths);
        mondayScience = findViewById(R.id.mondayScience);
        tuesdayMaths = findViewById(R.id.tuesdayMaths);
        tuesdayScience = findViewById(R.id.tuesdayScience);
        wednesdayMaths = findViewById(R.id.wednesdayMaths);
        wednesdayScience = findViewById(R.id.wednesdayScience);
        thursdayMaths = findViewById(R.id.thursdayMaths);
        thursdayScience = findViewById(R.id.thursdayScience);
        fridayMaths = findViewById(R.id.fridayMaths);
        fridayScience = findViewById(R.id.fridayScience);
        saturdayMaths = findViewById(R.id.saturdayMaths);
        saturdayScience = findViewById(R.id.saturdayScience);
        sundayMaths = findViewById(R.id.sundayMaths);
        sundayScience = findViewById(R.id.sundayScience);
        grade10_schedule = findViewById(R.id.grade10_schedule);
        grade11_schedule = findViewById(R.id.grade11_schedule);

        schedule_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveSchedule.setVisibility(View.GONE);
        mondayMaths.setEnabled(false);
        tuesdayMaths.setEnabled(false);
        wednesdayMaths.setEnabled(false);
        thursdayMaths.setEnabled(false);
        fridayMaths.setEnabled(false);
        saturdayMaths.setEnabled(false);
        sundayMaths.setEnabled(false);

        mondayScience.setEnabled(false);
        tuesdayScience.setEnabled(false);
        wednesdayScience.setEnabled(false);
        thursdayScience.setEnabled(false);
        fridayScience.setEnabled(false);
        saturdayScience.setEnabled(false);
        sundayScience.setEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("students");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getgrade = snapshot.child(uname).child("grade").getValue(Integer.class);
                getSchedule(getgrade);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        if (getgrade==10){
//            grade11_schedule.setVisibility(View.GONE);
//            grade10_schedule.setVisibility(View.VISIBLE);
//            grade10_schedule.setTextColor(getResources().getColor(R.color.white));
//            grade10_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
//            grade = "Grade 10";
//
//            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(grade).child("Maths");
//            databaseReference1.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        String s = dataSnapshot.getValue(String.class);
//                        mathsList.add(s);
//                    }
//                    mondayMaths.setText(mathsList.get(0));
//                    tuesdayMaths.setText(mathsList.get(1));
//                    wednesdayMaths.setText(mathsList.get(2));
//                    thursdayMaths.setText(mathsList.get(3));
//                    fridayMaths.setText(mathsList.get(4));
//                    saturdayMaths.setText(mathsList.get(5));
//                    sundayMaths.setText(mathsList.get(6));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(grade).child("Science");
//            databaseReference2.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        String s = dataSnapshot.getValue(String.class);
//                        scienceList.add(s);
//                    }
//                    mondayScience.setText(scienceList.get(0));
//                    tuesdayScience.setText(scienceList.get(1));
//                    wednesdayScience.setText(scienceList.get(2));
//                    thursdayScience.setText(scienceList.get(3));
//                    fridayScience.setText(scienceList.get(4));
//                    saturdayScience.setText(scienceList.get(5));
//                    sundayScience.setText(scienceList.get(6));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        } else {
//            grade11_schedule.setVisibility(View.VISIBLE);
//            grade10_schedule.setVisibility(View.GONE);
//            grade11_schedule.setTextColor(getResources().getColor(R.color.white));
//            grade11_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
//            grade = "Grade 11";
//
//            databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(grade).child("Maths");
//            databaseReference3.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        String s = dataSnapshot.getValue(String.class);
//                        mathsList.add(s);
//                    }
//                    mondayMaths.setText(mathsList.get(0));
//                    tuesdayMaths.setText(mathsList.get(1));
//                    wednesdayMaths.setText(mathsList.get(2));
//                    thursdayMaths.setText(mathsList.get(3));
//                    fridayMaths.setText(mathsList.get(4));
//                    saturdayMaths.setText(mathsList.get(5));
//                    sundayMaths.setText(mathsList.get(6));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//            databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(grade).child("Science");
//            databaseReference4.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        String s = dataSnapshot.getValue(String.class);
//                        scienceList.add(s);
//                    }
//                    mondayScience.setText(scienceList.get(0));
//                    tuesdayScience.setText(scienceList.get(1));
//                    wednesdayScience.setText(scienceList.get(2));
//                    thursdayScience.setText(scienceList.get(3));
//                    fridayScience.setText(scienceList.get(4));
//                    saturdayScience.setText(scienceList.get(5));
//                    sundayScience.setText(scienceList.get(6));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
    }

    private void getSchedule(int grade) {
        if (grade==10){
            grade11_schedule.setVisibility(View.GONE);
            grade10_schedule.setVisibility(View.VISIBLE);
            grade10_schedule.setTextColor(getResources().getColor(R.color.white));
            grade10_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
            String newgrade = "Grade 10";

            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(newgrade).child("Maths");
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        mathsList.add(s);
                    }
                    mondayMaths.setText(mathsList.get(0));
                    tuesdayMaths.setText(mathsList.get(1));
                    wednesdayMaths.setText(mathsList.get(2));
                    thursdayMaths.setText(mathsList.get(3));
                    fridayMaths.setText(mathsList.get(4));
                    saturdayMaths.setText(mathsList.get(5));
                    sundayMaths.setText(mathsList.get(6));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(newgrade).child("Science");
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        scienceList.add(s);
                    }
                    mondayScience.setText(scienceList.get(0));
                    tuesdayScience.setText(scienceList.get(1));
                    wednesdayScience.setText(scienceList.get(2));
                    thursdayScience.setText(scienceList.get(3));
                    fridayScience.setText(scienceList.get(4));
                    saturdayScience.setText(scienceList.get(5));
                    sundayScience.setText(scienceList.get(6));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            grade11_schedule.setVisibility(View.VISIBLE);
            grade10_schedule.setVisibility(View.GONE);
            grade11_schedule.setTextColor(getResources().getColor(R.color.white));
            grade11_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
            String newgrade = "Grade 11";

            databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(newgrade).child("Maths");
            databaseReference3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        mathsList.add(s);
                    }
                    mondayMaths.setText(mathsList.get(0));
                    tuesdayMaths.setText(mathsList.get(1));
                    wednesdayMaths.setText(mathsList.get(2));
                    thursdayMaths.setText(mathsList.get(3));
                    fridayMaths.setText(mathsList.get(4));
                    saturdayMaths.setText(mathsList.get(5));
                    sundayMaths.setText(mathsList.get(6));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Schedule").child(newgrade).child("Science");
            databaseReference4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String s = dataSnapshot.getValue(String.class);
                        scienceList.add(s);
                    }
                    mondayScience.setText(scienceList.get(0));
                    tuesdayScience.setText(scienceList.get(1));
                    wednesdayScience.setText(scienceList.get(2));
                    thursdayScience.setText(scienceList.get(3));
                    fridayScience.setText(scienceList.get(4));
                    saturdayScience.setText(scienceList.get(5));
                    sundayScience.setText(scienceList.get(6));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}