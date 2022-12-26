package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    ImageView schedule_back;
    EditText mondayMaths,mondayScience,tuesdayMaths,tuesdayScience,wednesdayMaths,wednesdayScience,thursdayMaths,thursdayScience,fridayMaths,fridayScience,saturdayMaths,saturdayScience,sundayMaths,sundayScience;
    TextView grade10_schedule,grade11_schedule;
    FloatingActionButton saveSchedule;
    List<String> mathsList;
    List<String> scienceList;

    List<String> getMathsList;
    List<String> getScienceList;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    static String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        mathsList = new ArrayList<>();
        scienceList = new ArrayList<>();
        getMathsList = new ArrayList<>();
        getScienceList = new ArrayList<>();

        schedule_back = findViewById(R.id.schedule_back);

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
        saveSchedule = findViewById(R.id.saveSchedule);


        schedule_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        grade10_schedule.setTextColor(getResources().getColor(R.color.white));
        grade10_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
        grade = "Grade 10";
        setGrade10Schedule();

        grade10_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade10_schedule.setTextColor(getResources().getColor(R.color.white));
                grade10_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
                grade11_schedule.setBackground(null);
                grade11_schedule.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = "Grade 10";

                setGrade10Schedule();
            }
        });

        grade11_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade11_schedule.setTextColor(getResources().getColor(R.color.white));
                grade11_schedule.setBackground(getDrawable(R.drawable.switch_trcks));
                grade10_schedule.setBackground(null);
                grade10_schedule.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = "Grade 11";

                setGrade11Schedule();
            }
        });

        saveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedule");
                if(validateTimeSlot()){
                    mathsList.clear();
                    scienceList.clear();
                    addToList();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(grade).child("Maths").setValue(mathsList);
                            databaseReference.child(grade).child("Science").setValue(scienceList);
                            TastyToast.makeText(ScheduleActivity.this, "Schedule updated Successfully", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

    private void setGrade11Schedule() {
        databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Schedule").child("Grade 11").child("Science");

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMathsList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String s = dataSnapshot.getValue(String.class);
                    getMathsList.add(s);
                }

                mondayScience.setText(getMathsList.get(0));
                tuesdayScience.setText(getMathsList.get(1));
                wednesdayScience.setText(getMathsList.get(2));
                thursdayScience.setText(getMathsList.get(3));
                fridayScience.setText(getMathsList.get(4));
                saturdayScience.setText(getMathsList.get(5));
                sundayScience.setText(getMathsList.get(6));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Schedule").child("Grade 11").child("Maths");

        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMathsList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String s = dataSnapshot.getValue(String.class);
                    getMathsList.add(s);
                }

                mondayMaths.setText(getMathsList.get(0));
                tuesdayMaths.setText(getMathsList.get(1));
                wednesdayMaths.setText(getMathsList.get(2));
                thursdayMaths.setText(getMathsList.get(3));
                fridayMaths.setText(getMathsList.get(4));
                saturdayMaths.setText(getMathsList.get(5));
                sundayMaths.setText(getMathsList.get(6));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setGrade10Schedule() {

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Schedule").child("Grade 10").child("Maths");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMathsList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String s = dataSnapshot.getValue(String.class);
                    getMathsList.add(s);
                }

                mondayMaths.setText(getMathsList.get(0));
                tuesdayMaths.setText(getMathsList.get(1));
                wednesdayMaths.setText(getMathsList.get(2));
                thursdayMaths.setText(getMathsList.get(3));
                fridayMaths.setText(getMathsList.get(4));
                saturdayMaths.setText(getMathsList.get(5));
                sundayMaths.setText(getMathsList.get(6));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Schedule").child("Grade 10").child("Science");

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMathsList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String s = dataSnapshot.getValue(String.class);
                    getMathsList.add(s);
                }

                mondayScience.setText(getMathsList.get(0));
                tuesdayScience.setText(getMathsList.get(1));
                wednesdayScience.setText(getMathsList.get(2));
                thursdayScience.setText(getMathsList.get(3));
                fridayScience.setText(getMathsList.get(4));
                saturdayScience.setText(getMathsList.get(5));
                sundayScience.setText(getMathsList.get(6));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addToList() {
        mathsList.add(mondayMaths.getText().toString());
        mathsList.add(tuesdayMaths.getText().toString());
        mathsList.add(wednesdayMaths.getText().toString());
        mathsList.add(thursdayMaths.getText().toString());
        mathsList.add(fridayMaths.getText().toString());
        mathsList.add(saturdayMaths.getText().toString());
        mathsList.add(sundayMaths.getText().toString());

        scienceList.add(mondayScience.getText().toString());
        scienceList.add(tuesdayScience.getText().toString());
        scienceList.add(wednesdayScience.getText().toString());
        scienceList.add(thursdayScience.getText().toString());
        scienceList.add(fridayScience.getText().toString());
        scienceList.add(saturdayScience.getText().toString());
        scienceList.add(sundayScience.getText().toString());
    }

    private boolean validateTimeSlot(){
        if(mondayMaths.getText().toString().equals("") && mondayScience.getText().toString().equals("") && tuesdayMaths.getText().toString().equals("") && tuesdayScience.getText().toString().equals("") && wednesdayMaths.getText().toString().equals("") && wednesdayScience.getText().toString().equals("") && thursdayMaths.getText().toString().equals("") && thursdayScience.getText().toString().equals("") && fridayMaths.getText().toString().equals("") && fridayScience.getText().toString().equals("") && saturdayMaths.getText().toString().equals("") && saturdayScience.getText().toString().equals("") && sundayMaths.getText().toString().equals("") && sundayScience.getText().toString().equals("")){
            TastyToast.makeText(this, "Please fill Time Slot", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            return false;
        }else
            return true;
    }
}