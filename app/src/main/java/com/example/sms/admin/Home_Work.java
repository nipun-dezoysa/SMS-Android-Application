package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.adapter.QuestionAdapter;
import com.example.sms.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class Home_Work extends AppCompatActivity {


    LinearLayout grade10, grade11, viewAnswers;
    ImageView back_btn;
    TextView gradeVal;
    public static String Grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

        grade10 = findViewById(R.id.grade10_hw);
        grade11 = findViewById(R.id.grade11_hw);
        back_btn = findViewById(R.id.home_work_back);
        gradeVal = findViewById(R.id.gradeValue);
        viewAnswers = findViewById(R.id.view_answers);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        grade10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Work.this, Grade10HW.class);
                startActivity(intent);
                Grade = "Grade 10";
            }
        });

        grade11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Work.this, Grade11HW.class);
                startActivity(intent);
                Grade = "Grade 11";
            }
        });

        viewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Work.this, ViewAnswers.class);
                startActivity(intent);
            }
        });

    }

}