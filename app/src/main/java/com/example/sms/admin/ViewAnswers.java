package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.AnswersAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewAnswers extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;

    AnswersAdapter answersAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        backButton = findViewById(R.id.answers_back);
        recyclerView = findViewById(R.id.answers_rv);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Answers");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    String fileName = dataSnapshot.getKey();
//                    String url = dataSnapshot.getValue(String.class);
//
//                    ((AnswersAdapter) recyclerView.getAdapter()).update(fileName, url);
//                }
                String fileName = snapshot.getKey();
                String url = snapshot.getValue(String.class);

                ((AnswersAdapter)recyclerView.getAdapter()).update(fileName, url);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        answersAdapter = new AnswersAdapter(recyclerView, ViewAnswers.this, new ArrayList<>(), new ArrayList<>());
        recyclerView.setAdapter(answersAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
    }
}