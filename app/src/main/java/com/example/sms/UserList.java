package com.example.sms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sms.adapter.MyAdapterStd;
import com.example.sms.interfaces.SelectListener;
import com.example.sms.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class UserList extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    ArrayList<Student> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    MyAdapterStd myAdapterStd;
    DatabaseReference databaseReference1;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ImageView button_back = findViewById(R.id.student_back);
        FloatingActionButton add_stud_btn = findViewById(R.id.addstudent);
        FloatingActionButton delete_all = findViewById(R.id.delete_all_entry);
        linearLayout = findViewById(R.id.userEntryLL);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_stud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this, Manage_User.class);
                startActivity(intent);
                finish();
            }
        });

        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete all records?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("students").removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                TastyToast.makeText(recyclerView.getContext(), "All records deleted!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                TastyToast.makeText(recyclerView.getContext(), "" + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                            }
                                        });

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

        recyclerView = findViewById(R.id.studrecyclerview);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("students");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapterStd = new MyAdapterStd(this, list, this);
        recyclerView.setAdapter(myAdapterStd);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Student student = dataSnapshot.getValue(Student.class);
                    String uname = dataSnapshot.getKey();
                    student.setUsername(uname);
                    list.add(student);
                }
                myAdapterStd.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemClicked(Student student) {
        EditText searchUname = findViewById(R.id.searchUname);
        String searchUsernameTxt = student.getUsername();
        TextView editRecords = findViewById(R.id.edit_std);
        PopupMenu popupMenu = new PopupMenu(UserList.this,recyclerView);
//        popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
//        popupMenu.setGravity(Gravity.END);
        popupMenu.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
//        popupMenu.setGravity(Gravity.CENTER_VERTICAL);
        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(UserList.this,Manage_User.class);
                startActivity(intent);
//                findViewById(R.id.edit_std).callOnClick();
                return false;
            }
        });

        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete " + searchUsernameTxt + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("students").child(student.getUsername()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                TastyToast.makeText(UserList.this, "" + student.getUsername() + " Deleted!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                TastyToast.makeText(UserList.this, "" + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        popupMenu.show();

    }

}