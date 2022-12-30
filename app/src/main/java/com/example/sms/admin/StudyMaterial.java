package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;
import com.example.sms.adapter.ReferenceAdapter;
import com.example.sms.model.Materials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterial extends AppCompatActivity {

    ImageView back_btn;
    EditText study_material_unitName,study_material_reference;
    AppCompatSpinner study_material_spinner_sub;
    Button study_material_add;
    RecyclerView study_material_recyclerview;

    List<String> sublist = new ArrayList<>();
    ArrayList<Materials> referenceList;
    ReferenceAdapter referenceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        back_btn = findViewById(R.id.study_mat_back);
        study_material_spinner_sub = findViewById(R.id.study_material_spinner_sub);
        study_material_unitName = findViewById(R.id.study_material_unitName);
        study_material_reference = findViewById(R.id.study_material_reference);
        study_material_add = findViewById(R.id.study_material_add);
        study_material_recyclerview = findViewById(R.id.study_material_recyclerview);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        sublist.add(0, "Subject");
        sublist.add("Maths");
        sublist.add("Science");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sublist);
        study_material_spinner_sub.setAdapter(arrayAdapter);

        loadAllReferences();

        study_material_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                String subjectText = study_material_spinner_sub.getSelectedItem().toString();
                String unitNameTxt = study_material_unitName.getText().toString();
                String reference = study_material_reference.getText().toString();
                final String referenceID = "" + System.currentTimeMillis();

                if (subjectText.isEmpty() || unitNameTxt.isEmpty() || reference.isEmpty()){
                    TastyToast.makeText(StudyMaterial.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                } else if (subjectText.equals("Subject")) {
                    TastyToast.makeText(StudyMaterial.this, "Please choose a subject", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                } else {

                    databaseReference.child("studyMaterials").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            databaseReference.child("studyMaterials").child(subjectText).child(referenceID).setValue(reference);

                            databaseReference.child("studyMaterials").child(referenceID).child("referenceID").setValue(referenceID);
                            databaseReference.child("studyMaterials").child(referenceID).child("subject").setValue(subjectText);
                            databaseReference.child("studyMaterials").child(referenceID).child("unitName").setValue(unitNameTxt);
                            databaseReference.child("studyMaterials").child(referenceID).child("referenceLink").setValue(reference);

                            TastyToast.makeText(StudyMaterial.this, "Reference Added", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            study_material_spinner_sub.setSelection(0);
                            study_material_unitName.getText().clear();
                            study_material_reference.getText().clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void loadAllReferences() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        referenceList = new ArrayList<>();
        study_material_recyclerview.setLayoutManager(linearLayoutManager);
        referenceAdapter = new ReferenceAdapter(this, referenceList);
        study_material_recyclerview.setAdapter(referenceAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference10.child("studyMaterials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

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


}