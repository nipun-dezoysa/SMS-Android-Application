package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class Grade11HW extends AppCompatActivity {

    private AppCompatSpinner spinner_sub11;
    private EditText homework_questions11, unitName;
    private Button add_questions11;
    private ImageView grade11_back;

    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;
    String Grade;
    Intent intent;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade11_hw);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        grade11_back = findViewById(R.id.grade11_back);
        homework_questions11 = findViewById(R.id.type_qstns11);
        add_questions11 = findViewById(R.id.add_qstns11);
        unitName = findViewById(R.id.unitName11);

        homework_rv = findViewById(R.id.homework_recyclerview11);

        subjectlist.add( 0,"Subject");
        subjectlist.add("Maths");
        subjectlist.add("Science");
        spinner_sub11 = findViewById(R.id.spinner_sub11);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,subjectlist);
        spinner_sub11.setAdapter(arrayAdapter);

//        Grade = intent.getStringExtra("Grade 10");

        loadAllQuestions();

        grade11_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        add_questions11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectText = spinner_sub11.getSelectedItem().toString();
                String homework_qstn_txt = homework_questions11.getText().toString();
                String unitName11Txt = unitName.getText().toString();
                final String timestamp = "" + System.currentTimeMillis();


                if (subjectText.isEmpty() || homework_qstn_txt.isEmpty()){
                    TastyToast.makeText(Grade11HW.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
                else if (subjectText.equals("Subject")) {
                    TastyToast.makeText(Grade11HW.this, "Please choose a subject", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else
                    databaseReference.child("homework").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("homework").child("Grade 11").child(timestamp).child("subjectName").setValue(subjectText);
                            databaseReference.child("homework").child("Grade 11").child(timestamp).child("question").setValue(homework_qstn_txt);
                            databaseReference.child("homework").child("Grade 11").child(timestamp).child("timestamp").setValue(timestamp);
                            databaseReference.child("homework").child("Grade 11").child(timestamp).child("unitName").setValue(unitName11Txt);

                            TastyToast.makeText(Grade11HW.this, "Question added successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            homework_questions11.getText().clear();
                            unitName.getText().clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(Grade11HW.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    });
            }
        });

    }

    private void loadAllQuestions() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        questionArrayList = new ArrayList<>();
        homework_rv.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapter(this, questionArrayList);
        homework_rv.setAdapter(questionAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        Question question = new Question();

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
}