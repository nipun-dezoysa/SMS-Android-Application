package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sms.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class UpdateActivity extends AppCompatActivity {

    ImageView update_back;
    EditText titleOfUpdate,contentOfUpdate;
    Button add_update;
    RecyclerView update_rv;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        update_back = findViewById(R.id.update_back);
        titleOfUpdate = findViewById(R.id.titleOfUpdate);
        contentOfUpdate = findViewById(R.id.contentOfUpdate);
        add_update = findViewById(R.id.add_update);
        update_rv = findViewById(R.id.update_rv);

        loadAllUpdates();

        update_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleOfUpdateTxt = titleOfUpdate.getText().toString();
                String contentOfUpdateTxt = contentOfUpdate.getText().toString();
                final String updateID = "" + System.currentTimeMillis();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                if (titleOfUpdateTxt.isEmpty() || contentOfUpdateTxt.isEmpty()){
                    TastyToast.makeText(UpdateActivity.this, "Fields cannot be empty", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                } else {
                    databaseReference.child("updates").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(updateID).child("Title").setValue(titleOfUpdateTxt);
                            databaseReference.child(updateID).child("Content").setValue(contentOfUpdateTxt);

                            TastyToast.makeText(UpdateActivity.this, "Updates Added Successfully", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                titleOfUpdate.getText().clear();
                contentOfUpdate.getText().clear();
            }
        });

    }

    private void loadAllUpdates() {

    }
}