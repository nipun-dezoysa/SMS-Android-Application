package com.example.sms.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.ExamsResults;
import com.example.sms.admin.ResultsHome;
import com.example.sms.model.Results;
import com.example.sms.model.Student;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    Context context;
    List<String> studentList;
    List<Results> results = ExamsResults.resultsList;
    List<Float> part1Marks, part2Marks, totalMarks;


    public ResultsAdapter(Context context, List<String> studentList, List<Float> part1Marks, List<Float> part2Marks, List<Float> totalMarks) {
        this.context = context;
        this.studentList = studentList;
        this.part1Marks = part1Marks;
        this.part2Marks = part2Marks;
        this.totalMarks = totalMarks;


    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.results_model, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, int position) {
        String s = studentList.get(position);
//        part1Marks.add(0f);
//        part2Marks.add(0f);
//        totalMarks.add(0f);
//        results.add(new Results(s,0f,0f,0f));
        holder.name.setText(s);
        holder.term.setText(ResultsHome.term);

        holder.part1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = holder.part1.getText().toString();
                int i = holder.getAdapterPosition();
                if (!value.equals("")) {
                    Float part1Mark = Float.parseFloat(value);
                    if (part1Mark <= 50) {
                        part1Marks.set(i, part1Mark);
                        Float total = part1Marks.get(i) + part2Marks.get(i);
                        holder.total.setText(total + "");
                        totalMarks.set(i, total);
                        results.set(i, new Results(studentList.get(i), part1Mark, part2Marks.get(i), total, getGrade(total)));
                        holder.grades.setText(getGrade(total));
                    } else {
                        TastyToast.makeText(context, "Marks should be less than or equal 50", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        holder.part1.setText(0 + "");

                    }
                }

            }
        });

        holder.part2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = holder.part2.getText().toString();

                int i = holder.getAdapterPosition();
                if (!value.equals("")) {
                    Float part2Mark = Float.parseFloat(holder.part2.getText().toString());
                    if (part2Mark <= 50) {
                        part2Marks.set(i, part2Mark);
                        Float total = part1Marks.get(i) + part2Marks.get(i);
                        holder.total.setText(total + "");
                        totalMarks.set(i, total);
                        results.set(i, new Results(studentList.get(i), part1Marks.get(i), part2Mark, total, getGrade(total)));
                        holder.grades.setText(getGrade(total));
                    } else {
                        TastyToast.makeText(context, "Marks should be less than or equal 50", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        holder.part2.setText(0 + "");

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView term, total, name, grades;
        EditText part1, part2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            term = itemView.findViewById(R.id.term);
            total = itemView.findViewById(R.id.total);
            name = itemView.findViewById(R.id.name);
            part1 = itemView.findViewById(R.id.part1);
            part2 = itemView.findViewById(R.id.part2);
            grades = itemView.findViewById(R.id.grades);
        }
    }

    private String getGrade(float marks) {
        if (marks >= 75) {
            return "A";
        } else if (marks >= 65) {
            return "B";
        } else if (marks >= 55) {
            return "C";
        } else if (marks >= 35) {
            return "S";
        } else {
            return "W";
        }
    }
}
