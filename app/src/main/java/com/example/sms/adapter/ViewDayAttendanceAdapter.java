package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewDayAttendanceAdapter extends RecyclerView.Adapter<ViewDayAttendanceAdapter.MyViewHolderViewAttendance>{

    Context context;
    ArrayList<String> list;
    private DatabaseReference databaseRef;

    public ViewDayAttendanceAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        databaseRef = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public ViewDayAttendanceAdapter.MyViewHolderViewAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_attendance_entry,parent,false);
        return new ViewDayAttendanceAdapter.MyViewHolderViewAttendance(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDayAttendanceAdapter.MyViewHolderViewAttendance holder, int position) {
        String s = list.get(position);
        holder.dayOfMonth.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolderViewAttendance extends RecyclerView.ViewHolder {
        TextView dayOfMonth;
        public MyViewHolderViewAttendance(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.attendance_day);
        }
    }
}
