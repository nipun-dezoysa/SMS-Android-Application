package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class EditUpdatesActivity extends AppCompatActivity {

    ImageView backButton;
    EditText editTitle, editContent;
    FloatingActionButton saveEditedUpdates;
    DatabaseReference databaseReference;

    Intent intent;

    String updateID,updateTitle,updateContent;

    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_updates);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        backButton = findViewById(R.id.editUpdates_back);
        editTitle = findViewById(R.id.editUpdateTitle);
        editContent = findViewById(R.id.editUpdateContent);
        saveEditedUpdates = findViewById(R.id.saveEditedUpdates);

        intent = getIntent();

        updateID = intent.getStringExtra("updateID");
        updateTitle = intent.getStringExtra("title");
        updateContent = intent.getStringExtra("content");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTitle.setText(updateTitle);
        editContent.setText(updateContent);


        saveEditedUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editTitleTxt = editTitle.getText().toString();
                String editContentTxt = editContent.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                if (editContentTxt.isEmpty() || editTitleTxt.isEmpty()){
                    TastyToast.makeText(EditUpdatesActivity.this, "Fields cannot be empty", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                } else databaseReference.child("updates").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("updates").child(updateID).child("updateID").setValue(updateID);
                        databaseReference.child("updates").child(updateID).child("title").setValue(editTitleTxt);
                        databaseReference.child("updates").child(updateID).child("content").setValue(editContentTxt);

                        TastyToast.makeText(EditUpdatesActivity.this, "Records Updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}