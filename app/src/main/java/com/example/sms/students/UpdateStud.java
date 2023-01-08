package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.UpdatesAdapter;
import com.example.sms.adapter.UpdatesStudAdapter;
import com.example.sms.model.Updates;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateStud extends AppCompatActivity {

    private UpdatesStudAdapter updatesStudAdapter;
    private ArrayList<Updates> updatesArrayList;

    RecyclerView updateStud_rv;
    DatabaseReference databaseReference;
    ImageView update_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stud);

        update_back = findViewById(R.id.update_stud_back);
        updateStud_rv = findViewById(R.id.updateStud_rv);
        update_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        updatesArrayList = new ArrayList<>();
        updateStud_rv.setLayoutManager(linearLayoutManager);
        updatesStudAdapter = new UpdatesStudAdapter(this, updatesArrayList);
        updateStud_rv.setAdapter(updatesStudAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        //get all updates
        DatabaseReference databaseReference00 = FirebaseDatabase.getInstance().getReference();
        databaseReference00.child("updates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updatesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Updates updates = dataSnapshot.getValue(Updates.class);
                    updatesArrayList.add(updates);
                }
                updatesStudAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}