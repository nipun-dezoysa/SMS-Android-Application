package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.model.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditHWActivity extends AppCompatActivity {

    ImageView backButton;
    EditText editSubject,editQuestion;
    FloatingActionButton saveEditedQuestioin;

    Intent intent;
    String subject,question,questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hwactivity);

        backButton = findViewById(R.id.editHW_back);
        editSubject = findViewById(R.id.editSubject);
        editQuestion = findViewById(R.id.editQuestion);
        saveEditedQuestioin = findViewById(R.id.saveEditedQuestion);


        questionId = intent.getStringExtra("timestamp");
        subject = intent.getStringExtra("subName");
        question = intent.getStringExtra("question");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editSubject.setText(subject);


    }
}