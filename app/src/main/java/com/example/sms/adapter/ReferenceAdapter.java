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
import com.example.sms.model.Question;

import java.util.ArrayList;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> referenceArrayList;

    public ReferenceAdapter(Context context, ArrayList<String> referenceArrayList) {
        this.context = context;
        this.referenceArrayList = referenceArrayList;
    }

    @NonNull
    @Override
    public ReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studymaterial_model,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceAdapter.ViewHolder holder, int position) {
        String s = referenceArrayList.get(position);


    }

    @Override
    public int getItemCount() {
        return referenceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sub_name_sm,unitName_sm,reference_sm;
        ImageView remove_reference,edit_reference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_name_sm = itemView.findViewById(R.id.sub_name_sm);
            unitName_sm = itemView.findViewById(R.id.unitName_sm);
            reference_sm = itemView.findViewById(R.id.reference_sm);
            remove_reference = itemView.findViewById(R.id.remove_reference);
            edit_reference = itemView.findViewById(R.id.edit_reference);
        }
    }
}
