package com.example.sms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.interfaces.SelectListener;
import com.example.sms.model.Student;

import java.util.ArrayList;

public class MyAdapterStd extends RecyclerView.Adapter<MyAdapterStd.MyViewHolderStd> {

    private Context context;
    private ArrayList<Student> list;
    private SelectListener selectListener;

    public MyAdapterStd(Context context, ArrayList<Student> list, SelectListener selectListener) {
        this.context = context;
        this.list = list;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public MyViewHolderStd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userentry, parent, false);
        return new MyViewHolderStd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderStd holder, @SuppressLint("RecyclerView") int position) {
        Student user = list.get(position);
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        holder.contact.setText(user.getContact());
        holder.subject.setText(user.getSubject());
        holder.grade.setText(user.getGrade() + "");


        holder.cardViewUE.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectListener.onItemClicked(list.get(position));

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolderStd extends RecyclerView.ViewHolder {
        TextView username, contact, email, subject, grade;
        CardView cardViewUE;

        public MyViewHolderStd(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textusername);
            contact = itemView.findViewById(R.id.textcontact);
            email = itemView.findViewById(R.id.textemail);
            subject = itemView.findViewById(R.id.textsub);
            grade = itemView.findViewById(R.id.textgrade);
            cardViewUE = itemView.findViewById(R.id.cardViewUE);
        }
    }
}
