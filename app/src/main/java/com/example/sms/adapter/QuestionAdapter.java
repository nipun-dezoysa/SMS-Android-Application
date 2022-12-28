package com.example.sms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.EditHWActivity;
import com.example.sms.admin.Grade10HW;
import com.example.sms.admin.Home_Work;
import com.example.sms.model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionView> {

    private Context context;
    private ArrayList<Question> questionArrayList;

    public QuestionAdapter(Context context, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.questionArrayList = questionArrayList;
    }



    @NonNull
    @Override
    public QuestionView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.question_model,parent,false);

        return new QuestionView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionView holder, int position) {

        Question question = questionArrayList.get(position);
        String timestamp = question.getTimestamp();
        holder.textQuestion.setText(question.getQuestion());
        holder.textSubject.setText(question.getSubjectName());
        holder.setUnitName.setText(question.getUnitName());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            Question question;
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("homework").child(Home_Work.Grade).child(timestamp).removeValue();
            }
        });



        holder.edit_qst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), EditHWActivity.class);
                intent1.putExtra("timestamp", timestamp);
                intent1.putExtra("subName", question.getSubjectName());
                intent1.putExtra("question", question.getQuestion());
                intent1.putExtra("grade", Home_Work.Grade);
                intent1.putExtra("setUnitName", question.getUnitName());
                v.getContext().startActivity(intent1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    class QuestionView extends RecyclerView.ViewHolder{

        public TextView textQuestion, textSubject, setUnitName;
        CardView cardView;
        ImageView remove,edit_qst;

        public QuestionView(@NonNull View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.text_sub_question);
            textSubject =  itemView.findViewById(R.id.text_sub_name);
            cardView = itemView.findViewById(R.id.cardView);
            remove = itemView.findViewById(R.id.remove_qstn);
            edit_qst = itemView.findViewById(R.id.edit_qstn);
            setUnitName = itemView.findViewById(R.id.setUnitName);

        }
    }
}
