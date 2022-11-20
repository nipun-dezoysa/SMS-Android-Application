package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.model.Question;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionView> {

    private Context context;
    private ArrayList<Question> questionArrayList;
//    private SelectListener selectListener;

    public QuestionAdapter(Context context, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.questionArrayList = questionArrayList;
//        this.selectListener = selectListener;
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

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectListener.onItemClicked(questionArrayList.get(position));
//            }
//        });

//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                TastyToast.makeText(context, "Clicked successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                return false;
////
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    class QuestionView extends RecyclerView.ViewHolder{

        TextView textQuestion, textSubject;
        CardView cardView;
        public QuestionView(@NonNull View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.text_sub_question);
            textSubject =  itemView.findViewById(R.id.text_sub_name);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
