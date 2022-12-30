package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class EditReference extends AppCompatActivity {

    Intent intent;
    String referenceID,subject,unitName,reference;

    ImageView editRef_back;
    AppCompatSpinner editSubRef;
    EditText editUnitNameRef,editReferenceLink;
    FloatingActionButton saveEditedReference;

    List<String> subjectlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reference);

        editRef_back = findViewById(R.id.editRef_back);
        editSubRef = findViewById(R.id.editSubRef);
        editUnitNameRef = findViewById(R.id.editUnitNameRef);
        editReferenceLink = findViewById(R.id.editReferenceLink);
        saveEditedReference = findViewById(R.id.saveEditedReference);


        intent = getIntent();

        referenceID = intent.getStringExtra("referenceID");
        subject = intent.getStringExtra("subject");
        unitName = intent.getStringExtra("unitName");
        reference = intent.getStringExtra("reference");

        subjectlist.add( 0,subject);

        if(subject.equals("Science"))
            subjectlist.add("Maths");
        else
            subjectlist.add("Science");



        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,subjectlist);
        editSubRef.setAdapter(arrayAdapter);

        editUnitNameRef.setText(unitName);
        editReferenceLink.setText(reference);

        editRef_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveEditedReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedSubject = editSubRef.getSelectedItem().toString();
                String editedUnitName = editUnitNameRef.getText().toString();
                String reference = editReferenceLink.getText().toString();


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                if (editedSubject.isEmpty() || editedUnitName.isEmpty() || reference.isEmpty()){

                    TastyToast.makeText(EditReference.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else databaseReference.child("studyMaterials").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child("studyMaterials").child(referenceID).child("referenceID").setValue(referenceID);
                        databaseReference.child("studyMaterials").child(referenceID).child("subject").setValue(editedSubject);
                        databaseReference.child("studyMaterials").child(referenceID).child("unitName").setValue(editedUnitName);
                        databaseReference.child("studyMaterials").child(referenceID).child("referenceLink").setValue(reference);

                        TastyToast.makeText(EditReference.this, "Reference Updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
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