package com.example.sms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.Add_Attendance;
import com.example.sms.model.Attendance;
import com.example.sms.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class Attendance_Adapter extends RecyclerView.Adapter<Attendance_Adapter.MyViewHolderAttendance> {

    Context context;
    ArrayList<Student> list;

    private DatabaseReference databaseRef;

    public Attendance_Adapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolderAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_entry, parent, false);
        return new MyViewHolderAttendance(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_Adapter.MyViewHolderAttendance holder, @SuppressLint("RecyclerView") int position) {
        Student user = list.get(position);
        holder.username.setText(user.getUsername());
        Add_Attendance.attendanceList.add(new Attendance(user.getUsername(), "Absent"));

        holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    Add_Attendance.attendanceList.set(position, new Attendance(user.getUsername(), "Present"));
                } else {
                    Add_Attendance.attendanceList.set(position, new Attendance(user.getUsername(), "Absent"));
                }
            }
        });


        Add_Attendance.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView attendance = Add_Attendance.attendance_date;
                String attendancetxt = attendance.getText().toString().replace('/', '-');

                if (attendancetxt.equals("Date")) {
                    TastyToast.makeText(context, "Please Select a Date", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else {
                    databaseRef.child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseRef.child("Attendance").child(attendance.getText().toString()).child(Add_Attendance.grade).setValue(Add_Attendance.attendanceList);
                            TastyToast.makeText(context, "Attendance Registered", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolderAttendance extends RecyclerView.ViewHolder {

        TextView username;
        CheckBox checkBox;

        public MyViewHolderAttendance(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.attendance_uname);
            checkBox = itemView.findViewById(R.id.checkBox_attendance);

        }
    }
}
