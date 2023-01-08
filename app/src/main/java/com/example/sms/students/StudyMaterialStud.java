package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.adapter.ReferenceAdapter;
import com.example.sms.adapter.StudentReferenceAdapter;
import com.example.sms.model.Materials;
import com.example.sms.others.OnlineUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class StudyMaterialStud extends AppCompatActivity {

    ImageView studymat_stud_back;

    ArrayList<Materials> referenceList;
    StudentReferenceAdapter referenceAdapter;
    RecyclerView study_material_recyclerview_stud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_stud);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        studymat_stud_back = findViewById(R.id.studymaterial_stud_back);
        study_material_recyclerview_stud = findViewById(R.id.study_material_recyclerview_stud);

        studymat_stud_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Paper.init(this);
        String uname = Paper.book().read(OnlineUsers.UserNamekey);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subject = snapshot.child(uname).child("subject").getValue(String.class);

                if (subject.equals("Maths Science")) {
                    loadAllReferences();
                } else if (subject.equals("Maths")) {
                    loadMathsReferences();
                } else if (subject.equals("Science")) {
                    loadScienceReferences();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void loadAllReferences() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        referenceList = new ArrayList<>();
        study_material_recyclerview_stud.setLayoutManager(linearLayoutManager);
        referenceAdapter = new StudentReferenceAdapter(this, referenceList);
        study_material_recyclerview_stud.setAdapter(referenceAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("studyMaterials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Materials materials = dataSnapshot.getValue(Materials.class);
                    referenceList.add(materials);
                }
                referenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMathsReferences() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        referenceList = new ArrayList<>();
        study_material_recyclerview_stud.setLayoutManager(linearLayoutManager);
        referenceAdapter = new StudentReferenceAdapter(this, referenceList);
        study_material_recyclerview_stud.setAdapter(referenceAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("studyMaterials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Materials materials = dataSnapshot.getValue(Materials.class);
                    if (materials.getSubject().equals("Maths")) {
                        referenceList.add(materials);
                    }
                }
                referenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadScienceReferences() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        referenceList = new ArrayList<>();
        study_material_recyclerview_stud.setLayoutManager(linearLayoutManager);
        referenceAdapter = new StudentReferenceAdapter(this, referenceList);
        study_material_recyclerview_stud.setAdapter(referenceAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference3.child("studyMaterials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Materials materials = dataSnapshot.getValue(Materials.class);
                    if (materials.getSubject().equals("Science")) {
                        referenceList.add(materials);
                    }
                }
                referenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}