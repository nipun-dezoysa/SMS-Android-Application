package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.model.Attendance;

import java.util.ArrayList;

public class ViewLoadAttendanceAdapter extends RecyclerView.Adapter<ViewLoadAttendanceAdapter.MyViewHolderViewAttendance> {

    Context context;
    ArrayList<Attendance> list;

    public ViewLoadAttendanceAdapter(Context context, ArrayList<Attendance> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewLoadAttendanceAdapter.MyViewHolderViewAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.load_attendance_entry,parent,false);
        return new ViewLoadAttendanceAdapter.MyViewHolderViewAttendance(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewLoadAttendanceAdapter.MyViewHolderViewAttendance holder, int position) {
        Attendance attendance = list.get(position);
        holder.attendance_username.setText(attendance.getUname());
        holder.status.setText(attendance.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolderViewAttendance extends RecyclerView.ViewHolder {

        TextView attendance_username;
        TextView status;

        public MyViewHolderViewAttendance(@NonNull View itemView) {
            super(itemView);

            attendance_username = itemView.findViewById(R.id.attendance_username);
            status = itemView.findViewById(R.id.status);
        }
    }
}
