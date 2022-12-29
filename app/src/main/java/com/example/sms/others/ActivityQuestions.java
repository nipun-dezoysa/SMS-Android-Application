package com.example.sms.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.model.Question;

import java.util.ArrayList;

public class ActivityQuestions extends AppCompatActivity {

    RecyclerView recyclerViewQuestions;
    ArrayList<Question> questionArrayList = new ArrayList<>();

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_questions);
//
//        recyclerViewQuestions = findViewById(R.id.recyclerview_questions);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
//        recyclerViewQuestions.setLayoutManager(layoutManager);
//
//        questionArrayList = (ArrayList<Question>) getIntent().getExtras().getSerializable("list");
//
//        recyclerViewQuestions.setAdapter(new QuestionAdapter(questionArrayList));
//    }
}
