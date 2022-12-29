package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.adapter.QuestionAdapterStud;
import com.example.sms.admin.Grade10HW;
import com.example.sms.admin.TeacherPageActivity;
import com.example.sms.model.Question;
import com.example.sms.model.Student;
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

import io.paperdb.Paper;

public class HomeworkStud extends AppCompatActivity {

    ImageView homework_stud_back;
    String uname;
    DatabaseReference databaseReference;
    private Button chooseFile,uploadFile;
    private TextView setFileName;
    Uri fileUri;

    private QuestionAdapterStud questionAdapter;
    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;
    String sub;
    int grade;
    ImageView removeqstn;
    Button submit;

    Dialog dialog;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference10;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_stud);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        removeqstn = findViewById(R.id.remove_qstn);
        submit = findViewById(R.id.submit_hw_answer);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUploadFilesPopUp();
            }
        });

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

    private void openUploadFilesPopUp() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.file_upload_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        chooseFile = dialog.findViewById(R.id.chooseFile);
        uploadFile = dialog.findViewById(R.id.uploadFile);
        setFileName = dialog.findViewById(R.id.fileName);

        storage = FirebaseStorage.getInstance();

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(HomeworkStud.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectFile();
                }
                else ActivityCompat.requestPermissions(HomeworkStud.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri!=null)
                    uploadFiles(fileUri);
                else TastyToast.makeText(HomeworkStud.this, "Please select a File", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });
        dialog.show();
    }

    private void uploadFiles(Uri fileUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        String fileName = uname+":"+System.currentTimeMillis();
//        String fileName = "Test";
        StorageReference storageReference = storage.getReference().child("Homework Answers").child(uname).child(fileName+"."+getFileExtension(fileUri));

        storageReference.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                databaseReference10  = FirebaseDatabase.getInstance().getReference().child("Answers").child(uname).child(fileName);
//                                databaseReference10  = FirebaseDatabase.getInstance().getReference().child("Answers").child(uname);

                                databaseReference10.setValue(uri.toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    TastyToast.makeText(HomeworkStud.this, "Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                } else
                                                    TastyToast.makeText(HomeworkStud.this, "Uploaded Failed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TastyToast.makeText(HomeworkStud.this, "Uploaded Failed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        // track upload progress
                        int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        progressDialog.setProgress(currentProgress);
                    }
                });
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(fileUri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectFile();
        } else TastyToast.makeText(this, "Please grand permission", TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    private void selectFile() {

        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        check whether a file is selected or not
        if (requestCode == 86 && resultCode == RESULT_OK && data!=null){
            fileUri = data.getData(); //returns uri of the selected file
            setFileName.setText(data.getData().getLastPathSegment());
        } else TastyToast.makeText(this, "Please Select a File", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
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