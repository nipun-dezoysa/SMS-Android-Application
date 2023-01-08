package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.model.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class EditHWActivity extends AppCompatActivity {

    ImageView backButton;
    EditText editQuestion, editUnitName;
    Spinner editSubject;
    FloatingActionButton saveEditedQuestion;
    String grade;

    DatabaseReference databaseReference;

    Intent intent;
    String subject, question, questionId, editUnitNameTxt;

    List<String> subjectlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hwactivity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        backButton = findViewById(R.id.editHW_back);
        editSubject = findViewById(R.id.editSubject);
        editQuestion = findViewById(R.id.editQuestion);
        saveEditedQuestion = findViewById(R.id.saveEditedQuestion);
        editUnitName = findViewById(R.id.editUnitName);


        intent = getIntent();

        questionId = intent.getStringExtra("timestamp");
        subject = intent.getStringExtra("subName");
        question = intent.getStringExtra("question");
        grade = intent.getStringExtra("grade");
        editUnitNameTxt = intent.getStringExtra("setUnitName");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        subjectlist.add(0, subject);

        if (subject.equals("Science"))
            subjectlist.add("Maths");
        else
            subjectlist.add("Science");
        editSubject = findViewById(R.id.editSubject);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subjectlist);
        editSubject.setAdapter(arrayAdapter);

        editQuestion.setText(question);
        editUnitName.setText(editUnitNameTxt);


        saveEditedQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectText = editSubject.getSelectedItem().toString();
                String homework_qstn_txt = editQuestion.getText().toString();
                String editedUnitName = editUnitName.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                if (subjectText.isEmpty() || homework_qstn_txt.isEmpty() || editedUnitName.isEmpty()) {
                    TastyToast.makeText(EditHWActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else
                    databaseReference.child("homework").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("homework").child(grade).child(questionId).child("subjectName").setValue(subjectText);
                            databaseReference.child("homework").child(grade).child(questionId).child("question").setValue(homework_qstn_txt);
                            databaseReference.child("homework").child(grade).child(questionId).child("timestamp").setValue(questionId);
                            databaseReference.child("homework").child(grade).child(questionId).child("unitName").setValue(editedUnitName);

                            TastyToast.makeText(EditHWActivity.this, "Question updated successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                finish();
            }
        });

    }
}