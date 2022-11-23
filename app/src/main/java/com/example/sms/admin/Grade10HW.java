package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class Grade10HW extends AppCompatActivity {

    private AppCompatSpinner spinner_sub;
    private EditText homework_questions;
    private Button add_questions;
    private ImageView grade10_back;

    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade10_hw);
        grade10_back = findViewById(R.id.grade10_back);
        homework_questions = findViewById(R.id.type_qstns);
        add_questions = findViewById(R.id.add_qstns);

        homework_rv = findViewById(R.id.homework_recyclerview);

        subjectlist.add( 0,"Subject");
        subjectlist.add("Maths");
        subjectlist.add("Science");
        spinner_sub = findViewById(R.id.spinner_sub);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,subjectlist);
        spinner_sub.setAdapter(arrayAdapter);

        loadAllQuestions();

        grade10_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        add_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectText = spinner_sub.getSelectedItem().toString();
                String homework_qstn_txt = homework_questions.getText().toString();
                final String timestamp = "" + System.currentTimeMillis();


                if (subjectText.isEmpty() || homework_qstn_txt.isEmpty()){
                    TastyToast.makeText(Grade10HW.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
                else if (subjectText.equals("Subject")) {
                    TastyToast.makeText(Grade10HW.this, "Please choose a subject", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else
                    databaseReference.child("homework").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("subjectName").setValue(subjectText);
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("question").setValue(homework_qstn_txt);

                            TastyToast.makeText(Grade10HW.this, "Question added successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(Grade10HW.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    });homework_questions.getText().clear();
            }
        });

    }

    private void loadAllQuestions() {
        questionArrayList = new ArrayList<>();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        homework_rv.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this, questionArrayList);
        homework_rv.setAdapter(questionAdapter);
        Question question = new Question();

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

                        //setup adapter
//                        questionAdapter = new QuestionAdapter(getApplicationContext(), questionArrayList);
                        //set adapter
//                        homework_rv.setAdapter(questionAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}