package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Attendance_Adapter extends RecyclerView.Adapter<Attendance_Adapter.MyViewHolderAttendance> {

    Context context;
    ArrayList<Student> list;

    private DatabaseReference databaseRef;

    public Attendance_Adapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
        databaseRef = FirebaseDatabase.getInstance().getReference("attendance");
    }

    @NonNull
    @Override
    public MyViewHolderAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_entry,parent,false);
        return new MyViewHolderAttendance(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_Adapter.MyViewHolderAttendance holder, int position) {
        Student user = list.get(position);
        holder.username.setText(user.getUsername());

        //checkbox ischecked

//        holder.save_student_attendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.checkBox.isChecked()){
//                    databaseRef.child(String.valueOf(holder.attendance_date)).setValue("Present");
//                } else {
//                    databaseRef.child(String.valueOf(holder.attendance_date)).setValue("Absent");
//                }
//            }
//        });
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//
//                }else {
//                }
//            }
//        });
//        holder.checkBox.setSelected(user.getCheckBoxSelected());

//        holder.save_student_attendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                databaseRef.child(String.valueOf(holder.username)).setValue(holder.checkBox.isChecked());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolderAttendance extends RecyclerView.ViewHolder{
//        public String checkbox;
        TextView username,grade10,grade11;
        TextView attendance_date;
        CheckBox checkBox;
        FloatingActionButton save_student_attendance;

        public MyViewHolderAttendance(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.attendance_uname);
            checkBox = itemView.findViewById(R.id.checkBox_attendance);

        }
    }
}
