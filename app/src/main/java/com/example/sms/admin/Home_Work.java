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

public class Home_Work extends AppCompatActivity{

    private AppCompatSpinner spinner_sub;
    private EditText homework_questions;
    private Button add_questions;
    private ImageView hw_back_button;
    private QuestionAdapter questionAdapter;

    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//    String uniqueKey = databaseReference.child("homework").push().getKey();
//    DatabaseReference uniqueKeyRef = databaseReference.child("homework").child(uniqueKey);

    DatabaseReference databaseReference1;
    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

        hw_back_button = findViewById(R.id.home_work_back);

        homework_questions = findViewById(R.id.type_qstns);
        add_questions = findViewById(R.id.add_qstns);
        homework_rv = findViewById(R.id.homework_recyclerview);

        subjectlist.add( 0,"Subject");
        subjectlist.add("Maths");
        subjectlist.add("Science");
        spinner_sub = (AppCompatSpinner) findViewById(R.id.spinner_sub);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,subjectlist);
        spinner_sub.setAdapter(arrayAdapter);

        loadAllQuestions();

        hw_back_button.setOnClickListener(new View.OnClickListener() {
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
//                String keytxt = uniqueKeyRef.getKey().toString();
                final String timestamp = "" + System.currentTimeMillis();

                if (subjectText.isEmpty() || homework_qstn_txt.isEmpty()){
                    TastyToast.makeText(Home_Work.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
                else if (subjectText.equals("Subject")) {
                    TastyToast.makeText(Home_Work.this, "Please choose a subject", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else
                    databaseReference.child("homework").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            databaseReference.child("homework").child("Grade 10").child(subjectText).setValue(homework_qstn_txt);
//                            databaseReference.child("homework").push().child("Grade 10").child(subjectText).setValue(homework_qstn_txt);
//                            databaseReference.child("homework").child("Grade 10").child(subjectText).push().setValue(homework_qstn_txt);
                            //set text working but overwriting
//                            databaseReference.child("homework").child("Grade 10").child("subjectName").setValue(subjectText);
//                            databaseReference.child("homework").child("Grade 10").child("question").setValue(homework_qstn_txt);

//                            databaseReference.child("homework").child(timestamp).child("Grade 10").child("subjectName").setValue(subjectText);
//                            databaseReference.child("homework").child(timestamp).child("Grade 10").child("question").setValue(homework_qstn_txt);
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("subjectName").setValue(subjectText);
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("question").setValue(homework_qstn_txt);

                            TastyToast.makeText(Home_Work.this, "Question added successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(Home_Work.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    });
            }
        });

    }

    private void loadAllQuestions() {
        questionArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        homework_rv.setLayoutManager(linearLayoutManager);
        Question question = new Question();

        //get all questions
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("homework")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionArrayList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Question question = dataSnapshot.getValue(Question.class);
                            questionArrayList.add(question);
                        }
                        //setup adapter
                        questionAdapter = new QuestionAdapter(getApplicationContext(), questionArrayList);
                        //set adapter
                        homework_rv.setAdapter(questionAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    @Override
//    public void onItemClicked(Question question) {
//        TastyToast.makeText(this, "Clicked successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//    }

//    private void loadAllQuestions(){
//        databaseReference1 = FirebaseDatabase.getInstance().getReference("homework");
//        questionArrayList = new ArrayList<>();
//        homework_rv.setLayoutManager(new LinearLayoutManager(this));
//        questionAdapter = new QuestionAdapter(this, questionArrayList);
//        homework_rv.setAdapter(questionAdapter);
//
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren())
//                {
//                    Question question = dataSnapshot.getValue(Question.class);
////                    String uname = dataSnapshot.getKey();
////                    student.setUsername(uname);
//                    questionArrayList.add(question);
//                }
//                questionAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}