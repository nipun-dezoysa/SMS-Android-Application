//package com.example.sms;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatSpinner;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.example.sms.model.Question;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
////public class HomeworkActivity extends AppCompatActivity implements View.OnClickListener {
//
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//    LinearLayout linearLayout;
//    Button addqstn;
//    Button submitqstn;
//
//    EditText question;
//    Spinner subject;
//
//    List<String> sublist = new ArrayList<>();
//    ArrayList<Question> questionArrayList = new ArrayList<>();
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_homework);
////
////        question = findViewById(R.id.EditMaterial);
////        subject = findViewById(R.id.spinner);
////
////        ImageView homewrk_back = findViewById(R.id.homework_back);
////        homewrk_back.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                finish();
////            }
////        });
////
////        linearLayout = findViewById(R.id.container);
////        addqstn = findViewById(R.id.addqstn);
////        submitqstn = findViewById(R.id.submitqstn);
//////        remove = findViewById(R.id.remove);
////
////
////        addqstn.setOnClickListener(this);
////        submitqstn.setOnClickListener(this);
////
////
////        sublist.add("Subject");
////        sublist.add("Maths");
////        sublist.add("Science");
////    }
////
////    @Override
////    public void onClick(View v) {
////        switch (v.getId()){
////
////            case R.id.addqstn:
////
////                addNew();
////
////                break;
////
////            case R.id.submitqstn:
////
////                if (checkIfValidAndRead()){
////
////                    Intent intent = new Intent(HomeworkActivity.this,ActivityQuestions.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("list", questionArrayList);
////                    intent.putExtras(bundle);
////                    startActivity(intent);
////
//////                    String txt_question = question.getText().toString();
//////                    String txt_subject = subject.getSelectedItem().toString();
//////
//////                    Question q = new Question(txt_question, txt_subject);
//////                    databaseReference.child("homework").setValue(q);
////                }
////                break;
////
////        }
////
////    }
////
////    private boolean checkIfValidAndRead() {
////        questionArrayList.clear();
////        boolean result = true;
////
////        for (int i=0; i<linearLayout.getChildCount();i++){
////
////            View view = linearLayout.getChildAt(i);
////
////            EditText editTextQuestion = view.findViewById(R.id.EditMaterial);
////            AppCompatSpinner spinner = view.findViewById(R.id.spinner);
////
////            Question question = new Question();
////
////            if (!editTextQuestion.getText().toString().equals("")){
////                question.setQuestion(editTextQuestion.getText().toString());
////            }else {
////                result = false;
////                break;
////            }
////
////            if (spinner.getSelectedItemPosition()!=0){
////                question.setSubjectName(sublist.get(spinner.getSelectedItemPosition()));
////            }else {
////                result = false;
////                break;
////            }
////
////            questionArrayList.add(question);
////        }
////
////        if (questionArrayList.size()==0){
////            result = false;
////
////            Toast.makeText(this, "Add Questions", Toast.LENGTH_SHORT).show();
////        }else if (!result){
////            Toast.makeText(this, "Add Relevant Fields", Toast.LENGTH_SHORT).show();
////        }
////
////
////        return result;
////    }
////
////    public void addNew(){
////        View view = getLayoutInflater().inflate(R.layout.material, null, false);
////
////        EditText editText = view.findViewById(R.id.EditMaterial);
////        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
////        Button closeButton = view.findViewById(R.id.CloseBtn);
////
////
////        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sublist);
////        spinner.setAdapter(arrayAdapter);
////
////        closeButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                removeView(view);
////            }
////        });
////
////
////        linearLayout.addView(view);
////
////    }
////
////    private void removeView(View view) {
////
////        linearLayout.removeView(view);
////    }
//
//
//}