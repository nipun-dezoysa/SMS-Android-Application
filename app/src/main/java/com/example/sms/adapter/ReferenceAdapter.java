package com.example.sms.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.EditHWActivity;
import com.example.sms.admin.EditReference;
import com.example.sms.admin.Home_Work;
import com.example.sms.model.Materials;
import com.example.sms.model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Materials> referenceArrayList;

    public ReferenceAdapter(Context context, ArrayList<Materials> referenceArrayList) {
        this.context = context;
        this.referenceArrayList = referenceArrayList;
    }

    @NonNull
    @Override
    public ReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studymaterial_model, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceAdapter.ViewHolder holder, int position) {
        Materials materials = referenceArrayList.get(position);

        holder.sub_name_sm.setText(materials.getSubject());
        holder.unitName_sm.setText(materials.getUnitName());
        holder.reference_sm.setText(materials.getReferenceLink());

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
                    // if Chrome browser not installed, allow user to choose instead
                    intent.setPackage(null);
                    context.startActivity(intent);
                }
            }
        });

        holder.edit_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), EditReference.class);
                intent1.putExtra("referenceID", materials.getReferenceID());
                intent1.putExtra("subject", materials.getSubject());
                intent1.putExtra("unitName", materials.getUnitName());
                intent1.putExtra("reference", materials.getReferenceLink());
                v.getContext().startActivity(intent1);

            }
        });

        holder.remove_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                reference.child("studyMaterials").child(materials.getReferenceID()).removeValue();

                                TastyToast.makeText(context, "Deleted Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return referenceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sub_name_sm, unitName_sm, reference_sm;
        ImageView remove_reference, edit_reference;

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
