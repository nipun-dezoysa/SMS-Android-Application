package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.adapter.QuestionAdapterStud;
import com.example.sms.admin.TeacherPageActivity;
import com.example.sms.model.Question;
import com.example.sms.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeworkStud extends AppCompatActivity {

    ImageView homework_stud_back;
    String uname;
    DatabaseReference databaseReference;

    private QuestionAdapterStud questionAdapter;
    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;
    String sub;
    int grade;
    ImageView removeqstn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_stud);

        removeqstn = findViewById(R.id.remove_qstn);
//        removeqstn.setVisibility(View.GONE);

        homework_stud_back = findViewById(R.id.homework_stud_back);

        homework_rv = findViewById(R.id.homeworkstd_recyclerview);

        homework_stud_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Paper.init(HomeworkStud.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);



        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sub = snapshot.child(uname).child("subject").getValue(String.class);
                grade = snapshot.child(uname).child("grade").getValue(int.class);



                if (grade==10 && sub.equals("Maths")){
                    loadAllGrade10MathsQuestions();
                } else if (grade==10 && sub.equals("Science")){
                    loadAllGrade10ScienceQuestions();
                } else if (grade==10 && sub.equals("Maths Science")){
                    loadAllGrade10MathsScienceQuestions();
                } else if (grade==11 && sub.equals("Maths")){
                    loadAllGrade11MathsQuestions();
                } else if (grade==11 && sub.equals("Science")){
                    loadAllGrade11ScienceQuestions();
                } else if (grade==11 && sub.equals("Maths Science")){
                    loadAllGrade11MathsScienceQuestions();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void loadAllGrade11MathsScienceQuestions() {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            questionArrayList = new ArrayList<>();
            homework_rv.setLayoutManager(linearLayoutManager);
            questionAdapter = new QuestionAdapterStud(this, questionArrayList);
            homework_rv.setAdapter(questionAdapter);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);


            //get all questions
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("homework").child("Grade 11")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            questionArrayList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Question question = dataSnapshot.getValue(Question.class);

                                    questionArrayList.add(question);

                            }
                            questionAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

    private void loadAllGrade11ScienceQuestions() {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            questionArrayList = new ArrayList<>();
            homework_rv.setLayoutManager(linearLayoutManager);
            questionAdapter = new QuestionAdapterStud(this, questionArrayList);
            homework_rv.setAdapter(questionAdapter);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            Question question = new Question();
            Student student = new Student();


            //get all questions
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("homework").child("Grade 11")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            questionArrayList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Question question = dataSnapshot.getValue(Question.class);
                                if (question.getSubjectName().equals("Science")){
                                    questionArrayList.add(question);
                                }

                            }
                            questionAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

    }

    private void loadAllGrade10MathsScienceQuestions() {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            questionArrayList = new ArrayList<>();
            homework_rv.setLayoutManager(linearLayoutManager);
            questionAdapter = new QuestionAdapterStud(this, questionArrayList);
            homework_rv.setAdapter(questionAdapter);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);


            //get all questions
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("homework").child("Grade 10")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            questionArrayList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Question question = dataSnapshot.getValue(Question.class);
                                    questionArrayList.add(question);
                            }
                            questionAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




    }

    private void loadAllGrade10ScienceQuestions() {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            questionArrayList = new ArrayList<>();
            homework_rv.setLayoutManager(linearLayoutManager);
            questionAdapter = new QuestionAdapterStud(this, questionArrayList);
            homework_rv.setAdapter(questionAdapter);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            Question question = new Question();
            Student student = new Student();


            //get all questions
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("homework").child("Grade 10")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            questionArrayList.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Question question = dataSnapshot.getValue(Question.class);
                                if (question.getSubjectName().equals("Science")){
                                    questionArrayList.add(question);
                                }

                            }
                            questionAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


    }

    private void loadAllGrade10MathsQuestions() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        questionArrayList = new ArrayList<>();
        homework_rv.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapterStud(this, questionArrayList);
        homework_rv.setAdapter(questionAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        //get all questions
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("homework").child("Grade 10")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionArrayList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Question question = dataSnapshot.getValue(Question.class);
                            if (question.getSubjectName().equals("Maths")){
                                questionArrayList.add(question);
                            }

                        }
                        questionAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllGrade11MathsQuestions() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        questionArrayList = new ArrayList<>();
        homework_rv.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapterStud(this, questionArrayList);
        homework_rv.setAdapter(questionAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        //get all questions
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("homework").child("Grade 11")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionArrayList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Question question = dataSnapshot.getValue(Question.class);
                            if (question.getSubjectName().equals("Maths")){
                                questionArrayList.add(question);
                            }
                        }
                        questionAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}