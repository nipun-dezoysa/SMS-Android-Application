package com.example.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.model.Updates;

import java.util.ArrayList;

public class UpdatesStudAdapter extends RecyclerView.Adapter<UpdatesStudAdapter.UpdatesStudView>{

    private Context context;
    private ArrayList<Updates> updatesArrayList;

    public UpdatesStudAdapter(Context context, ArrayList<Updates> updatesArrayList) {
        this.context = context;
        this.updatesArrayList = updatesArrayList;
    }

    @NonNull
    @Override
    public UpdatesStudAdapter.UpdatesStudView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.update_model,parent,false);

        return new UpdatesStudAdapter.UpdatesStudView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatesStudAdapter.UpdatesStudView holder, int position) {

        Updates updates = updatesArrayList.get(position);

        holder.titleUpdate.setText(updates.getTitle());
        holder.contentUpdate.setText(updates.getContent());

        holder.editUpdate.setVisibility(View.GONE);
        holder.removeUpdate.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return updatesArrayList.size();
    }

    public class UpdatesStudView extends RecyclerView.ViewHolder {

        TextView titleUpdate,contentUpdate;
        ImageView removeUpdate,editUpdate;

        public UpdatesStudView(@NonNull View itemView) {
            super(itemView);

            titleUpdate = itemView.findViewById(R.id.titleUpdate);
            contentUpdate = itemView.findViewById(R.id.contentUpdate);
            removeUpdate = itemView.findViewById(R.id.removeUpdate);
            editUpdate = itemView.findViewById(R.id.editUpdate);
        }
    }
}
