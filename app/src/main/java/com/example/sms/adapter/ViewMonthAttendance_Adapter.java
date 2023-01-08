package com.example.sms.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.DayAttendance;
import com.example.sms.admin.ReportActivity;
import com.example.sms.admin.ViewAttendance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewMonthAttendance_Adapter extends RecyclerView.Adapter<ViewMonthAttendance_Adapter.MyViewHolderViewAttendance> {

    Context context;
    ArrayList<String> list;
    private DatabaseReference databaseRef;

    public ViewMonthAttendance_Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        databaseRef = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public ViewMonthAttendance_Adapter.MyViewHolderViewAttendance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_month, parent, false);
        return new ViewMonthAttendance_Adapter.MyViewHolderViewAttendance(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMonthAttendance_Adapter.MyViewHolderViewAttendance holder, int position) {
        String s = list.get(position);

        int m = Integer.parseInt(s);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        cal.set(Calendar.MONTH, m - 1);
        String month_name = month_date.format(cal.getTime());

        Log.e("", "" + month_name);

        holder.attendace_month.setText(month_name);

        holder.attendace_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DayAttendance.class);
                intent.putExtra("month", s);
                ViewAttendance.month = s;
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolderViewAttendance extends RecyclerView.ViewHolder {
        TextView attendace_month;

        public MyViewHolderViewAttendance(View itemView) {
            super(itemView);
            attendace_month = itemView.findViewById(R.id.attendance_month);
        }
    }
}
