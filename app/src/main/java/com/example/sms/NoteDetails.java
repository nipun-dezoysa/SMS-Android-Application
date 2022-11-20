package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetails extends AppCompatActivity {

    private TextView titleOfNoteDetail, contentOfNoteDetail;
    FloatingActionButton goToEditNote;

    private String uname;
    ImageView noteDetailBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Intent i = getIntent();
        uname = i.getStringExtra("uname");

        titleOfNoteDetail = findViewById(R.id.titleOfNoteDetail);
        contentOfNoteDetail = findViewById(R.id.contentOfNoteDetail);
        goToEditNote = findViewById(R.id.goToEditNote);
        noteDetailBack = findViewById(R.id.noteDetail_back);

        noteDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent data = getIntent();

        goToEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                intent.putExtra("uname", uname);
                finish();
                v.getContext().startActivity(intent);

            }
        });

        contentOfNoteDetail.setText(data.getStringExtra("content"));
        titleOfNoteDetail.setText(data.getStringExtra("title"));
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId()==android.R.id.home)
//        {
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}