package com.example.sms.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.model.Materials;

import java.util.ArrayList;

public class StudentReferenceAdapter extends RecyclerView.Adapter<StudentReferenceAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Materials> referenceArrayList;

    public StudentReferenceAdapter(Context context, ArrayList<Materials> referenceArrayList) {
        this.context = context;
        this.referenceArrayList = referenceArrayList;
    }

    @NonNull
    @Override
    public StudentReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studymaterial_model,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentReferenceAdapter.ViewHolder holder, int position) {
        Materials materials = referenceArrayList.get(position);

        holder.sub_name_sm.setText(materials.getSubject());
        holder.unitName_sm.setText(materials.getUnitName());
        holder.reference_sm.setText(materials.getReferenceLink());

        holder.edit_reference.setVisibility(View.GONE);
        holder.remove_reference.setVisibility(View.GONE);

        holder.reference_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(materials.getReferenceLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");

                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return referenceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
