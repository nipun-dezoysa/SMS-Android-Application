package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.example.sms.interfaces.EditStudentDetailsCallback;
import com.example.sms.model.EditStudentDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Manage_User extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    EditText searchUname;
    String uname;

    EditText passwordTouch;

    String editoutputPassword;
    String editencryptedPassword;

    String getpwdTxt;
    String getpwd1Txt;

    String outputPassword1;
    String decryptedPassword;

    String outputPassword;
    String encryptedPassword;
    String AES = "AES";

    EditText Username;
    EditText EmailID;
    EditText Contact;
    EditText Password;
    EditText ConfirmPwd;

    TextView editRecords;
    TextView register;
    LinearLayout registerxml;
    LinearLayout recordsxml;
    Button searchbtn;
    Button registerbtn;
    Button savechange;
    Button viewbtn;
    Button deletebtn;
    EditText editEmail;
    EditText editContact;
    EditText editpwd;
    EditText editpwd1;
    ImageView manage_std_back;


    String UsernameTxt;
    String EmailIDTxt;
    String ContactTxt;
    String SubjectTxt;
    int GradeInt;
    String PasswordTxt;

    CheckBox checkBoxMaths;
    CheckBox checkBoxScience;
    RadioButton radioBtnGrade10;
    RadioButton radioBtnGrade11;

    CheckBox checkBoxMathsEdit;
    CheckBox checkBoxScienceEdit;
    RadioButton radioBtnGrade10Edit;
    RadioButton radioBtnGrade11Edit;

    LinearLayout checkboxView;
    RadioGroup radioView;

    String searchUnameTxt;


    EditStudentDetails editStudentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);


        manage_std_back = findViewById(R.id.mng_std_back);
        manage_std_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editRecords = findViewById(R.id.edit_std);
        register = findViewById(R.id.register_std);
        registerxml = findViewById(R.id.register_layout);
        recordsxml = findViewById(R.id.register_layout1);
        searchbtn = findViewById(R.id.search_btn);
        registerbtn = findViewById(R.id.register_btn);
        savechange = findViewById(R.id.save_btn);
        viewbtn = findViewById(R.id.view_btn);
        deletebtn = findViewById(R.id.delete_btn);
        editEmail = findViewById(R.id.eeid);
        editContact = findViewById(R.id.ecnid);
        editpwd = findViewById(R.id.cpid);
        editpwd1 = findViewById(R.id.ccpid);
        checkBoxMaths = findViewById(R.id.checkBoxMaths);
        checkBoxScience = findViewById(R.id.checkBoxScience);
        radioBtnGrade10 = findViewById(R.id.radioBtnGrade10);
        radioBtnGrade11 = findViewById(R.id.radioBtnGrade11);

        checkBoxMathsEdit = findViewById(R.id.checkBoxMathsedit);
        checkBoxScienceEdit = findViewById(R.id.checkBoxScienceedit);
        radioBtnGrade10Edit = findViewById(R.id.radioBtnGrade10edit);
        radioBtnGrade11Edit = findViewById(R.id.radioBtnGrade11edit);

        checkboxView = findViewById(R.id.checkboxview);
        radioView = findViewById(R.id.radioview);

        passwordTouch = findViewById(R.id.password);

        passwordTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (event.getRawX() >= (passwordTouch.getRight()-
                            passwordTouch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Password Patterns")
                                .setMessage("Please follow the password pattern with at least one digit, one upper case letter, one lower case letter and one special symbol (“@#$%”). Password length should be between 6 and 15")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                    }
                                })
                                .show();
//
                    }
                }
                return false;
            }
        });


        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_User.this, UserList.class);
                TastyToast.makeText(Manage_User.this, "Tap hold to edit/delete", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                startActivity(intent);

//                finish();
            }
        });

        editRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRecords.setTextColor(getResources().getColor(R.color.white));
                editRecords.setBackground(getDrawable(R.drawable.switch_trcks));
                register.setBackground(null);
                register.setTextColor(getResources().getColor(R.color.teal_700));
                registerxml.setVisibility(View.GONE);
                recordsxml.setVisibility(View.VISIBLE);
                registerbtn.setVisibility(View.GONE);
                searchbtn.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.GONE);
                editContact.setVisibility(View.GONE);
//                editSubject.setVisibility(View.GONE);
                editpwd.setVisibility(View.GONE);
                editpwd1.setVisibility(View.GONE);
                searchbtn.setVisibility(View.VISIBLE);
                viewbtn.setVisibility(View.VISIBLE);
                savechange.setVisibility(View.GONE);
                checkboxView.setVisibility(View.GONE);
                radioView.setVisibility(View.GONE);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setTextColor(getResources().getColor(R.color.white));
                register.setBackground(getDrawable(R.drawable.switch_trcks));
                editRecords.setBackground(null);
                editRecords.setTextColor(getResources().getColor(R.color.teal_700));
                registerxml.setVisibility(View.VISIBLE);
                recordsxml.setVisibility(View.GONE);
                searchbtn.setVisibility(View.GONE);
                viewbtn.setVisibility(View.GONE);
                registerbtn.setVisibility(View.VISIBLE);
                savechange.setVisibility(View.GONE);
                deletebtn.setVisibility(View.GONE);
                checkboxView.setVisibility(View.GONE);
                radioView.setVisibility(View.GONE);
                searchUname.setEnabled(true);
                searchUname.getText().clear();
            }
        });

        searchUname = findViewById(R.id.searchUname);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                searchUnameTxt = searchUname.getText().toString();

                if (searchUnameTxt.isEmpty()) {
                    Toast.makeText(Manage_User.this, "Please give a Username", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(searchUnameTxt)) {
                                Toast.makeText(Manage_User.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            } else {
                                getEditStudentDetails(new EditStudentDetailsCallback() {
                                    @Override
                                    public void onCallback(EditStudentDetails ESD) {
                                        editStudentDetails = ESD;
                                        editEmail.setText(editStudentDetails.getEmail());
                                        editContact.setText(editStudentDetails.getContact());
                                        getSubject(editStudentDetails.getSubject());
                                        getGrade(editStudentDetails.getGrade());
//                                        editpwd.setText(editStudentDetails.getPassword());
//                                        editpwd1.setText(editStudentDetails.getPassword());
                                    }
                                });

                                searchUname.setEnabled(false);
                                editEmail.setVisibility(View.VISIBLE);
                                editContact.setVisibility(View.VISIBLE);
//                                editSubject.setVisibility(View.VISIBLE);
                                editpwd.setVisibility(View.VISIBLE);
                                editpwd1.setVisibility(View.VISIBLE);
                                searchbtn.setVisibility(View.GONE);
                                viewbtn.setVisibility(View.GONE);
                                savechange.setVisibility(View.VISIBLE);
                                deletebtn.setVisibility(View.VISIBLE);
                                checkboxView.setVisibility(View.VISIBLE);
                                radioView.setVisibility(View.VISIBLE);

//                                if (editStudentDetails!=null){
//                                    editEmail.setText(editStudentDetails.getEmail());
//                                    editContact.setText(editStudentDetails.getContact());
//                                    editpwd.setText(editStudentDetails.getPassword());
//                                    editpwd1.setText(editStudentDetails.getPassword());
//
//                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }

            }
        });

        savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uNameTxt = searchUname.getText().toString();
                String editEmailTxt = editEmail.getText().toString();
                String editContactTxt = editContact.getText().toString();
                String editpwdTxt = editpwd.getText().toString();
                String editpwd1Txt = editpwd1.getText().toString();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";
                final String mobileNumberPattern= "^\\d{10}$";

                if (uNameTxt.isEmpty() || editEmailTxt.isEmpty() || editContactTxt.isEmpty() || editpwdTxt.isEmpty() || editpwd1Txt.isEmpty() || !(checkBoxMathsEdit.isChecked() || checkBoxScienceEdit.isChecked())) {
                    TastyToast.makeText(Manage_User.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (!editEmailTxt.matches(emailPattern)){
                    TastyToast.makeText(Manage_User.this, "Please enter a valid email address", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (!editContactTxt.matches(mobileNumberPattern)){
                    TastyToast.makeText(Manage_User.this, "Please enter 10 digit contact number", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (editpwdTxt.length() < 6){
                    TastyToast.makeText(Manage_User.this, "Password should be more than 6 characters", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                }
                else if (editpwdTxt.length() > 15){
                    TastyToast.makeText(Manage_User.this, "Password should not be exceed 15 characters ", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else if (!editpwdTxt.matches(passwordPattern)){
                    TastyToast.makeText(Manage_User.this, "Please follow password pattern to make a strong password", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                }
                else if (!editpwdTxt.equals(editpwd1Txt)) {
                    TastyToast.makeText(Manage_User.this, "Passwords do not match", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                } else {
                    searchUname.setEnabled(true);
                    searchUname.getText().clear();
                    editEmail.setVisibility(View.GONE);
                    editContact.setVisibility(View.GONE);
//                    editSubject.setVisibility(View.GONE);
                    editpwd.setVisibility(View.GONE);
                    editpwd1.setVisibility(View.GONE);
                    searchbtn.setVisibility(View.VISIBLE);
                    viewbtn.setVisibility(View.VISIBLE);
                    savechange.setVisibility(View.GONE);
                    deletebtn.setVisibility(View.GONE);
                    checkboxView.setVisibility(View.GONE);
                    radioView.setVisibility(View.GONE);

                    try {
                        editoutputPassword = encrypt(editpwdTxt, editpwd1Txt);
                        editencryptedPassword = editoutputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    EditStudentDetails ESD = new EditStudentDetails(uNameTxt, editEmailTxt, editContactTxt, setSubject(), setGrade(), editencryptedPassword);
                    databaseReference.child("students").child(searchUnameTxt).setValue(ESD);

                    TastyToast.makeText(Manage_User.this, "Saved your changes", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete " + searchUnameTxt + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("students").child(searchUnameTxt).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                searchUname.getText().clear();
                                                editEmail.getText().clear();
                                                editContact.getText().clear();
//                                                editSubject.getText().clear();
                                                editpwd.getText().clear();
                                                editpwd1.getText().clear();

                                                TastyToast.makeText(v.getContext(), "" + searchUnameTxt + " Deleted!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                TastyToast.makeText(v.getContext(), "" + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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


        Username = findViewById(R.id.Username);
        EmailID = findViewById(R.id.email);
        Contact = findViewById(R.id.contact);
        Password = findViewById(R.id.password);
        ConfirmPwd = findViewById(R.id.confirmpwd);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsernameTxt = Username.getText().toString();
                EmailIDTxt = EmailID.getText().toString();
                ContactTxt = Contact.getText().toString();
                SubjectTxt = getSubject();
                GradeInt = getGrade();
                PasswordTxt = Password.getText().toString();
                String ConfirmPwdTxt = ConfirmPwd.getText().toString();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";
                final String mobileNumberPattern= "^\\d{10}$";
//                String emailPattern = "^(.+)@(.+)$";

                if (UsernameTxt.isEmpty() || EmailIDTxt.isEmpty() || ContactTxt.isEmpty() || PasswordTxt.isEmpty() || ConfirmPwdTxt.isEmpty() || subjectValidation() || gradeValidation()) {
                    TastyToast.makeText(v.getContext(), "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (!EmailIDTxt.matches(emailPattern)){
                    TastyToast.makeText(v.getContext(), "Please enter a valid email address", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (!ContactTxt.matches(mobileNumberPattern)){
                    TastyToast.makeText(v.getContext(), "Please enter 10 digit contact number", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (PasswordTxt.length() < 6){
                    TastyToast.makeText(v.getContext(), "Password should be more than 6 characters", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (PasswordTxt.length() > 15){
                    TastyToast.makeText(v.getContext(), "Password should not be exceed 15 characters ", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (!PasswordTxt.matches(passwordPattern)){
                    TastyToast.makeText(v.getContext(), "Please follow password pattern to make a strong password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (!PasswordTxt.equals(ConfirmPwdTxt)) {
                    TastyToast.makeText(v.getContext(), "Passwords do not match", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                    else {
                    try {
                        outputPassword = encrypt(PasswordTxt, ConfirmPwdTxt);
                        encryptedPassword = outputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RegisterAccount();
                }

            }
        });


    }

    private String  encrypt(String data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

//    private String  decrypt(String data, String password) throws Exception {
//        SecretKeySpec key = generateKey(password);
//        Cipher c = Cipher.getInstance(AES);
//        c.init(Cipher.DECRYPT_MODE, key);
////        byte[] decVal = c.doFinal(data.getBytes());
//        byte[] decVal= Base64.decode(data, Base64.DEFAULT);
//        String decryptedValue = new String((decVal));
//        return decryptedValue;
//    }



    private void RegisterAccount() {

        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            check if username is not registered before
                if (snapshot.hasChild(UsernameTxt)) {
                    TastyToast.makeText(Manage_User.this, "User is already registered", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    databaseReference.child("students").child(UsernameTxt).child("email").setValue(EmailIDTxt);
                    databaseReference.child("students").child(UsernameTxt).child("contact").setValue(ContactTxt);
                    databaseReference.child("students").child(UsernameTxt).child("subject").setValue(SubjectTxt);
                    databaseReference.child("students").child(UsernameTxt).child("grade").setValue(GradeInt);
                    databaseReference.child("students").child(UsernameTxt).child("password").setValue(encryptedPassword);


                    TastyToast.makeText(Manage_User.this, "User registered successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                finish();
                    Username.getText().clear();
                    EmailID.getText().clear();
                    Contact.getText().clear();
                    checkBoxMaths.setChecked(false);
                    checkBoxScience.setChecked(false);
                    radioBtnGrade10.setChecked(false);
                    radioBtnGrade11.setChecked(false);
                    Password.getText().clear();
                    ConfirmPwd.getText().clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(Manage_User.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });


    }

    private boolean subjectValidation(){
           if (!checkBoxMaths.isChecked() && !checkBoxScience.isChecked()){
               return true;
           }
           return false;
    }

    private String getSubject(){
        if (checkBoxMaths.isChecked()&&checkBoxScience.isChecked()){
            return "Maths Science";
        }
        else if(checkBoxMaths.isChecked()){
            return "Maths";
        }
        else return "Science";
    }

    private boolean gradeValidation(){
        if (!radioBtnGrade10.isChecked() && !radioBtnGrade11.isChecked()){
            return true;
        }
        return false;
    }

    private int getGrade(){
        if (radioBtnGrade10.isChecked()){
            return 10;
        }
        else return 11;
    }

    private void getEditStudentDetails(EditStudentDetailsCallback studentDetailsCallback) {

        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(searchUnameTxt)) {
                    EditStudentDetails value = snapshot.child(searchUnameTxt).getValue(EditStudentDetails.class);
                    studentDetailsCallback.onCallback(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSubject(String subject){
        if (subject.equals("Maths Science")){
            checkBoxMathsEdit.setChecked(true);
            checkBoxScienceEdit.setChecked(true);
        }
        else if (subject.equals("Maths")){
            checkBoxMathsEdit.setChecked(true);
            checkBoxScienceEdit.setChecked(false);
        }

        else {
            checkBoxScienceEdit.setChecked(true);
            checkBoxMathsEdit.setChecked(false);
        }
    }

    private String setSubject(){
        if (checkBoxMathsEdit.isChecked() && checkBoxScienceEdit.isChecked()){
            return "Maths Science";
        }
        else if (checkBoxMathsEdit.isChecked()){
            return "Maths";
        }
        else if (checkBoxScienceEdit.isChecked()){
            return "Science";
        }
        return "";
    }

    private void getGrade(int grade){
        if (grade==10){
            radioBtnGrade10Edit.setChecked(true);
            radioBtnGrade11Edit.setChecked(false);
        }
        else {
            radioBtnGrade11Edit.setChecked(true);
            radioBtnGrade10Edit.setChecked(false);
        }
    }

    private int setGrade(){
        if (radioBtnGrade10Edit.isChecked()){
            return 10;
        }
        else return 11;
    }

}