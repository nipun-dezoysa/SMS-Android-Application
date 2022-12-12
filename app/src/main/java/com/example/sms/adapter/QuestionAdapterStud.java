package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.Home_Work;
import com.example.sms.model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapterStud extends RecyclerView.Adapter<QuestionAdapterStud.QuestionView> {

    private Context context;
    private ArrayList<Question> questionArrayList;

    public QuestionAdapterStud(Context context, ArrayList<Question> questionArrayList) {
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
        holder.textQuestion.setText(question.getQuestion());
        holder.textSubject.setText(question.getSubjectName());


        holder.remove.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    class QuestionView extends RecyclerView.ViewHolder{

        TextView textQuestion, textSubject;
        CardView cardView;
        ImageView remove;
        public QuestionView(@NonNull View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.text_sub_question);
            textSubject =  itemView.findViewById(R.id.text_sub_name);
            cardView = itemView.findViewById(R.id.cardView);
            remove = itemView.findViewById(R.id.remove_qstn);

        }
    }
}
