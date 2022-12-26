package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.example.sms.adapter.MyAdapterStd;
import com.example.sms.interfaces.SelectListener;
import com.example.sms.model.EditStudentDetails;
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

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserList extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    ArrayList<Student> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    MyAdapterStd myAdapterStd;
    DatabaseReference databaseReference1;
    LinearLayout linearLayout;

    ProgressBar progressBar;


    EditText findUname;
    EditText changeEmail;
    EditText changeContact;
    CheckBox changeCheckBoxMaths;
    CheckBox changeCheckBoxScience;
    RadioButton changeRadioBtnGrade10;
    RadioButton changeRadioBtnGrade11;
    Button buttonSaveChange;
    Button buttonCancelChange;
    String studentTxt;
    String studentEmailTxt;
    String studentContactTxt;
    String studentPwdTxt;
    String subjectCheck;
    int gradeCheck;



    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ImageView button_back = findViewById(R.id.student_back);
        FloatingActionButton add_stud_btn = findViewById(R.id.addstudent);
        FloatingActionButton delete_all = findViewById(R.id.delete_all_entry);
        linearLayout = findViewById(R.id.userEntryLL);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_stud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this, Manage_User.class);
                startActivity(intent);
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
    public void onItemClicked(Student student)
    {
        studentTxt = student.getUsername();
        studentEmailTxt = student.getEmail();
        studentContactTxt = student.getContact();
        studentPwdTxt = student.getPassword();
        subjectCheck = student.getSubject();
        gradeCheck = student.getGrade();


        AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
        builder.setTitle("Edit/Delete")
                .setMessage("You can edit or delete " + student.getUsername())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteStudentRecord();

                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openEditRecordsPopUp(studentTxt);
                    }
                }).show();

    }


    private void deleteStudentRecord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete " + studentTxt + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child("students").child(studentTxt).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        TastyToast.makeText(UserList.this, "" + studentTxt + " Deleted!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
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
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void openEditRecordsPopUp(String studuname) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_records);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        progressBar = dialog.findViewById(R.id.progressBarPop1);
        findUname = dialog.findViewById(R.id.findUname);
        changeEmail = dialog.findViewById(R.id.changeEmail);
        changeContact = dialog.findViewById(R.id.changeContact);
        changeCheckBoxMaths = dialog.findViewById(R.id.changeCheckBoxMaths);
        changeCheckBoxScience = dialog.findViewById(R.id.changeCheckBoxScience);
        changeRadioBtnGrade10 = dialog.findViewById(R.id.changeRadioBtnGrade10);
        changeRadioBtnGrade11 = dialog.findViewById(R.id.changeRadioBtnGrade11);;
        buttonSaveChange = dialog.findViewById(R.id.buttonSaveChange);
        buttonCancelChange = dialog.findViewById(R.id.buttonCancelChange);

        findUname.setText(studuname);
        changeEmail.setText(studentEmailTxt);
        changeContact.setText(studentContactTxt);
        getSubject(subjectCheck);
        getGrade(gradeCheck);
        findUname.setEnabled(false);

        buttonSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String usernameTxt = findUname.getText().toString();
                String emailTxt = changeEmail.getText().toString();
                String contactTxt = changeContact.getText().toString();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String mobileNumberPattern= "^\\d{10}$";

                if (usernameTxt.isEmpty() || emailTxt.isEmpty() || contactTxt.isEmpty() || !(changeCheckBoxMaths.isChecked() || changeCheckBoxScience.isChecked())) {
                    TastyToast.makeText(UserList.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (!emailTxt.matches(emailPattern)){
                    TastyToast.makeText(UserList.this, "Please enter a valid email address", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (!contactTxt.matches(mobileNumberPattern)){
                    TastyToast.makeText(UserList.this, "Please enter 10 digit contact number", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else {

//                    mistakenly created 2 student models
//                    Student s = new Student(usernameTxt, contactTxt, emailTxt, setSubject(), setGrade(), changeEncryptedPassword);
//                    databaseReference.child("students").child(studuname).setValue(s);

                    databaseReference.child("students").child(studuname).child("contact").setValue(contactTxt);
                    databaseReference.child("students").child(studuname).child("email").setValue(emailTxt);
                    databaseReference.child("students").child(studuname).child("subject").setValue(setSubject());
                    databaseReference.child("students").child(studuname).child("grade").setValue(setGrade());
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    TastyToast.makeText(UserList.this, "Saved your changes", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    finish();
                    startActivity(getIntent());

                }

            }
        });

        buttonCancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(UserList.this, "Nothing changed", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });


        dialog.show();

    }



//    private boolean subjectValidation(){
//        if (!changeCheckBoxMaths.isChecked() && !changeCheckBoxScience.isChecked()){
//            return true;
//        }
//        return false;
//    }

//    private String getSubject(){
//        if (changeCheckBoxMaths.isChecked()&&changeCheckBoxScience.isChecked()){
//            return "Maths Science";
//        }
//        else if(changeCheckBoxMaths.isChecked()){
//            return "Maths";
//        }
//        else return "Science";
//    }

//    private boolean gradeValidation(){
//        if (!changeRadioBtnGrade10.isChecked() && !changeRadioBtnGrade11.isChecked()){
//            return true;
//        }
//        return false;
//    }

//    private int getGrade(){
//        if (changeRadioBtnGrade10.isChecked()){
//            return 10;
//        }
//        else return 11;
//    }


    private void getSubject(String subject) {
        if (subject.equals("Maths Science")){
            changeCheckBoxMaths.setChecked(true);
            changeCheckBoxScience.setChecked(true);
        }
        else if (subject.equals("Maths")){
            changeCheckBoxMaths.setChecked(true);
            changeCheckBoxScience.setChecked(false);
        }

        else {
            changeCheckBoxScience.setChecked(true);
            changeCheckBoxMaths.setChecked(false);
        }
    }

    private String setSubject(){
        if (changeCheckBoxMaths.isChecked() && changeCheckBoxScience.isChecked()){
            return "Maths Science";
        }
        else if (changeCheckBoxMaths.isChecked()){
            return "Maths";
        }
        else if (changeCheckBoxScience.isChecked()){
            return "Science";
        }
        return "";
    }

    private void getGrade(int grade) {
        if (grade==10){
            changeRadioBtnGrade10.setChecked(true);
            changeRadioBtnGrade11.setChecked(false);
        }
        else {
            changeRadioBtnGrade11.setChecked(true);
            changeRadioBtnGrade10.setChecked(false);
        }
    }

    private int setGrade(){
        if (changeRadioBtnGrade10.isChecked()){
            return 10;
        }
        else return 11;
    }

//    {
//        EditText searchUname = findViewById(R.id.searchUname);
//        String searchUsernameTxt = student.getUsername();
//        TextView editRecords = findViewById(R.id.edit_std);
//        PopupMenu popupMenu = new PopupMenu(UserList.this,recyclerView);
////        popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
////        popupMenu.setGravity(Gravity.END);
//        popupMenu.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
////        popupMenu.setGravity(Gravity.CENTER_VERTICAL);
//        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(UserList.this,Manage_User.class);
//                startActivity(intent);
////                findViewById(R.id.edit_std).callOnClick();
//                return false;
//            }
//        });
//
//        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
//                builder.setTitle("Delete")
//                        .setMessage("Are you sure you want to delete " + searchUsernameTxt + "?")
//                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                databaseReference.child("students").child(student.getUsername()).removeValue()
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                TastyToast.makeText(UserList.this, "" + student.getUsername() + " Deleted!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                                finish();
//                                                startActivity(getIntent());
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                TastyToast.makeText(UserList.this, "" + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                                            }
//                                        });
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//
//                return false;
//            }
//        });
//        popupMenu.show();
//
//    }

}