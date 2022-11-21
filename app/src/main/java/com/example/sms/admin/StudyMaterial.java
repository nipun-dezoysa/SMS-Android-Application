package com.example.sms.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sms.R;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterial extends AppCompatActivity {

    LinearLayout linearLayout;
    Button add;

    List<String> sublist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        ImageView back_btn = (ImageView) findViewById(R.id.study_mat_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayout = findViewById(R.id.container);
        add = findViewById(R.id.add);
//        remove = findViewById(R.id.remove);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNew();
            }
        });

        sublist.add("Subject");
        sublist.add("Maths");
        sublist.add("Science");




//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                linearLayout.removeView();
//            }
//        });
    }




    public void addNew(){
        View view = getLayoutInflater().inflate(R.layout.material, null, false);

        EditText editText = (EditText)view.findViewById(R.id.EditMaterial);
        AppCompatSpinner spinner = (AppCompatSpinner)view.findViewById(R.id.spinner);
        Button closeButton = (Button)view.findViewById(R.id.CloseBtn);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sublist);
        spinner.setAdapter(arrayAdapter);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
            }
        });


        linearLayout.addView(view);

    }

    private void removeView(View view) {

        linearLayout.removeView(view);
    }


}