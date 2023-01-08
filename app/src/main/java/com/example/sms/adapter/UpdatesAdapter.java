package com.example.sms.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.admin.EditHWActivity;
import com.example.sms.admin.EditUpdatesActivity;
import com.example.sms.admin.Home_Work;
import com.example.sms.model.Question;
import com.example.sms.model.Updates;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.UpdatesView> {

    private Context context;
    private ArrayList<Updates> updatesArrayList;

    public UpdatesAdapter(Context context, ArrayList<Updates> updatesArrayList) {
        this.context = context;
        this.updatesArrayList = updatesArrayList;
    }

    @NonNull
    @Override
    public UpdatesAdapter.UpdatesView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.update_model, parent, false);

        return new UpdatesView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatesAdapter.UpdatesView holder, int position) {

        Updates updates = updatesArrayList.get(position);
        String updateID = updates.getUpdateID();

        holder.titleUpdate.setText(updates.getTitle());
        holder.contentUpdate.setText(updates.getContent());

        holder.removeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("updates").child(updateID).removeValue();
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

        holder.editUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), EditUpdatesActivity.class);
                intent1.putExtra("updateID", updateID);
                intent1.putExtra("title", updates.getTitle());
                intent1.putExtra("content", updates.getContent());
                v.getContext().startActivity(intent1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return updatesArrayList.size();
    }


    public class UpdatesView extends RecyclerView.ViewHolder {

        TextView titleUpdate, contentUpdate;
        ImageView removeUpdate, editUpdate;

        public UpdatesView(@NonNull View itemView) {
            super(itemView);

            titleUpdate = itemView.findViewById(R.id.titleUpdate);
            contentUpdate = itemView.findViewById(R.id.contentUpdate);
            removeUpdate = itemView.findViewById(R.id.removeUpdate);
            editUpdate = itemView.findViewById(R.id.editUpdate);
        }
    }
}
