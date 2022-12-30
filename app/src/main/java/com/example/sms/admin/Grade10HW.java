package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class Grade10HW extends AppCompatActivity {

    private AppCompatSpinner spinner_sub;
    private EditText homework_questions, unitName;
    private Button add_questions,chooseFile,uploadFile;
    private TextView setFileName;
    private ImageView grade10_back;

    Uri fileUri;

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
        unitName = findViewById(R.id.unitName);
        add_questions = findViewById(R.id.add_qstns);
        chooseFile = findViewById(R.id.chooseFile);
        uploadFile = findViewById(R.id.uploadFile);
        setFileName = findViewById(R.id.fileName);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


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
                String unitNameTxt = unitName.getText().toString();
                final String timestamp = "" + System.currentTimeMillis();


                if (subjectText.isEmpty() || homework_qstn_txt.isEmpty() || unitNameTxt.isEmpty()){
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
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("timestamp").setValue(timestamp);
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("unitName").setValue(unitNameTxt);

                            TastyToast.makeText(Grade10HW.this, "Question added successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            homework_questions.getText().clear();
                            unitName.getText().clear();
                            spinner_sub.setSelection(0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(Grade10HW.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
//        Question question = new Question();

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
}