package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {


    Intent data;
    EditText editTitleOfNote, editContentOfNote;
    FloatingActionButton saveEditedNote;
    ImageView editNoteBackButton;
    TextView titleOfNoteDetail, contentOfNoteDetail;

    private String uname;

    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent i = getIntent();
        uname = i.getStringExtra("uname");

        editTitleOfNote = findViewById(R.id.editTitleofNote);
        editContentOfNote = findViewById(R.id.editContentOfNote);
        saveEditedNote = findViewById(R.id.saveEditedNote);
        editNoteBackButton = findViewById(R.id.editNote_back);
        titleOfNoteDetail = findViewById(R.id.titleOfNoteDetail);
        contentOfNoteDetail = findViewById(R.id.contentOfNoteDetail);

        editNoteBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveEditedNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTitle = editTitleOfNote.getText().toString();
                String newContent = editContentOfNote.getText().toString();

                if (newTitle.isEmpty()||newContent.isEmpty())
                {
                    TastyToast.makeText(EditNoteActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    return;
                }
                else
                {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(uname).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newTitle);
                    note.put("content", newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            TastyToast.makeText(EditNoteActivity.this, "Note updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            TastyToast.makeText(EditNoteActivity.this, "Update failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                            finish();
                        }
                    });
                }

            }
        });

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        editContentOfNote.setText(noteContent);
        editTitleOfNote.setText(noteTitle);
//        titleOfNoteDetail.setText(noteTitle);
//        contentOfNoteDetail.setText(noteContent);
    }


}