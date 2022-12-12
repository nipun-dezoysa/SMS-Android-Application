package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.admin.TeacherPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MySubjects extends AppCompatActivity {

    ImageView mysub_back;
    String uname;
    String sub;

    CardView maths;
    CardView science;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);

        Paper.init(MySubjects.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);

        mysub_back = findViewById(R.id.mysub_back);
        maths = findViewById(R.id.maths);
        science = findViewById(R.id.science);

        mysub_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sub = snapshot.child(uname).child("subject").getValue(String.class);

                if (sub.equals("Maths")){
                    maths.setVisibility(View.VISIBLE);
                    science.setVisibility(View.INVISIBLE);
                } else if (sub.equals("Science")){
                    science.setVisibility(View.VISIBLE);
                    maths.setVisibility(View.INVISIBLE);
                } else {
                    science.setVisibility(View.VISIBLE);
                    maths.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}