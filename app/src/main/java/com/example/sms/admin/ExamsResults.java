package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapterStud;
import com.example.sms.adapter.ResultsAdapter;
import com.example.sms.model.Results;
import com.example.sms.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class ExamsResults extends AppCompatActivity {

    ImageView examresult_back;
    FloatingActionButton save_results;
    TextView grade10_list_results, grade11_list_results, maths_list_results, science_list_results;
    RecyclerView resultsList_rv;

    int grade;
    String subject;

    List<String> studentList;
    List<Float> part1, part2, total;
    public static List<Results> resultsList = new ArrayList<>();

    ResultsAdapter resultsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_results);

        examresult_back = findViewById(R.id.exams_results_back);
        grade10_list_results = findViewById(R.id.grade10_list_results);
        grade11_list_results = findViewById(R.id.grade11_list_results);
        maths_list_results = findViewById(R.id.maths_list_results);
        science_list_results = findViewById(R.id.science_list_results);
        resultsList_rv = findViewById(R.id.resultsList_rv);
        save_results = findViewById(R.id.save_results);

        examresult_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        grade10_list_results.setTextColor(getResources().getColor(R.color.white));
        grade10_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
        grade11_list_results.setBackground(null);
        grade11_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
        grade = 10;
        maths_list_results.setTextColor(getResources().getColor(R.color.white));
        maths_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
        science_list_results.setBackground(null);
        science_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
        subject = "Maths";

//        loadGrade10MathsList();
        loadData(grade, subject);

        grade10_list_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade10_list_results.setTextColor(getResources().getColor(R.color.white));
                grade10_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
                grade11_list_results.setBackground(null);
                grade11_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = 10;
                loadData(grade, subject);

            }
        });

        grade11_list_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade11_list_results.setTextColor(getResources().getColor(R.color.white));
                grade11_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
                grade10_list_results.setBackground(null);
                grade10_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = 11;

                loadData(grade, subject);
            }
        });

        maths_list_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maths_list_results.setTextColor(getResources().getColor(R.color.white));
                maths_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
                science_list_results.setBackground(null);
                science_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
                subject = "Maths";

                loadData(grade, subject);
            }
        });

        science_list_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                science_list_results.setTextColor(getResources().getColor(R.color.white));
                science_list_results.setBackground(getDrawable(R.drawable.switch_trcks));
                maths_list_results.setBackground(null);
                maths_list_results.setTextColor(getResources().getColor(R.color.darkgreen));
                subject = "Science";

                loadData(grade, subject);

            }
        });

        save_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Results").child("2023")
                        .child(ResultsHome.term).child(grade + "").child(subject).setValue(resultsList);
                TastyToast.makeText(ExamsResults.this, "Marks Added Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });

    }


    private void loadData(int grade, String subject) {

        part1 = new ArrayList<>();
        part2 = new ArrayList<>();
        total = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        studentList = new ArrayList<>();
        resultsList_rv.setLayoutManager(linearLayoutManager);
        resultsAdapter = new ResultsAdapter(this, studentList, part1, part2, total);
        resultsList_rv.setAdapter(resultsAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resultsList.clear();
                studentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student.getGrade() == grade) {
                        if (student.getSubject().equals(subject) || student.getSubject().equals("Maths Science")) {
                            studentList.add(dataSnapshot.getKey());
                            part1.add(0f);
                            part2.add(0f);
                            total.add(0f);
                            resultsList.add(new Results(dataSnapshot.getKey(), 0f, 0f, 0f, "W"));
                        }
                    }
                }
                resultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}