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
    private EditText homework_questions;
    private Button add_questions,chooseFile,uploadFile;
    private TextView setFileName;
    private ImageView grade10_back;

    Uri fileUri;

    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionArrayList;
    private RecyclerView homework_rv;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    DatabaseReference databaseReference10;
    FirebaseStorage storage;

    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade10_hw);

        grade10_back = findViewById(R.id.grade10_back);
        homework_questions = findViewById(R.id.type_qstns);
        add_questions = findViewById(R.id.add_qstns);
        chooseFile = findViewById(R.id.chooseFile);
        uploadFile = findViewById(R.id.uploadFile);
        setFileName = findViewById(R.id.fileName);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        storage = FirebaseStorage.getInstance();

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Grade10HW.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectFile();
                }
                else ActivityCompat.requestPermissions(Grade10HW.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri!=null)
                uploadFiles(fileUri);
                else TastyToast.makeText(Grade10HW.this, "", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });

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
                            databaseReference.child("homework").child("Grade 10").child(timestamp).child("timestamp").setValue(timestamp);

                            TastyToast.makeText(Grade10HW.this, "Question added successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            homework_questions.getText().clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(Grade10HW.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    });
            }
        });

    }

    private void uploadFiles(Uri fileUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        String fileName = System.currentTimeMillis()+"";
//        String fileName = "Test";
        StorageReference storageReference = storage.getReference().child("Uploaded Files").child(fileName+"."+getFileExtension(fileUri));

        storageReference.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                databaseReference10  = FirebaseDatabase.getInstance().getReference().child(fileName);

                                databaseReference10.setValue(uri.toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    TastyToast.makeText(Grade10HW.this, "Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                    progressDialog.dismiss();
                                                } else
                                                    TastyToast.makeText(Grade10HW.this, "Uploaded Failed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                            }
                                        });
                            }
                        });
//                        String url = storageReference.getDownloadUrl().toString(); //url of the uploaded file
                        //store the url in real-time
//                        databaseReference10  = FirebaseDatabase.getInstance().getReference().child(fileName);
//
//                        databaseReference10.setValue(url)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful())
//                                            TastyToast.makeText(Grade10HW.this, "Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                        else
//                                            TastyToast.makeText(Grade10HW.this, "Uploaded Failed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                    }
//                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TastyToast.makeText(Grade10HW.this, "Uploaded Failed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
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