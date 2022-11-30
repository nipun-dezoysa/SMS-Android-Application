package com.example.sms.students;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sms.CropperActivity;
import com.example.sms.NotesActivity;
import com.example.sms.PopUp;
import com.example.sms.R;
import com.example.sms.ScheduleStud;
import com.example.sms.interfaces.UserStudentCallback;
import com.example.sms.model.UserStudent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class StudentPageActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextView etDate;
    DatePickerDialog.OnDateSetListener setListener;

    private String uname;
    Dialog dialog;

    String currentOutputPassword;
    String currentEncryptedPassword;
    String AES = "AES";
    String newOutputPassword;
    String newEncryptedPassword;

    UserStudent student;
    ProgressBar progressBar;

    ImageView profileEditStd;
    Uri dwnUri;
    Uri imageUri;

    EditText currentPwd;
    EditText newPwd;
    EditText confirmNewPwd;
    Button savePwd;
    Button cancelPwd;

    StorageReference storageReference;

    CircleImageView cropView;
    ActivityResultLauncher<String> mGetContent;
    TextView cam_uri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    EditText fullName;
    EditText nickName;
    EditText contactNo;
    EditText emailID;
    EditText address;
    CircleImageView profilepic;

    TextView welcometext;
    TextView date;
    TextView time;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentpage);

        date = (TextView)findViewById(R.id.current_date1);
        time = (TextView)findViewById(R.id.current_time1);
        welcometext = (TextView)findViewById(R.id.dashboardtxt1);
        date.setText(getCurrentDate());
        time.setText(getCurrentTime());

        LinearLayout mysub = (LinearLayout) findViewById(R.id.mysub);
        LinearLayout attendance = (LinearLayout) findViewById(R.id.attendance);
        LinearLayout schedule = (LinearLayout) findViewById(R.id.schedule);
        LinearLayout homework = (LinearLayout) findViewById(R.id.homework);
        LinearLayout studyMaterial = (LinearLayout) findViewById(R.id.studymaterial);
        LinearLayout notes = (LinearLayout) findViewById(R.id.notes);
        LinearLayout exam_result = (LinearLayout) findViewById(R.id.exams_results_view);
        LinearLayout report = (LinearLayout) findViewById(R.id.reports);
        LinearLayout update = (LinearLayout) findViewById(R.id.updates);
        LinearLayout txt_rec = (LinearLayout) findViewById(R.id.txt_rec_std);

        ImageView logout = (ImageView) findViewById(R.id.logoutpopupbtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);

                popupMenu.getMenu().add("Change Password").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        openChangePassword();
                        return false;
                    }
                });
                popupMenu.getMenu().add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

//                        Intent logoutIntent = new Intent(v.getContext(), StudentLoginActivity.class);
//                        v.getContext().startActivity(logoutIntent);
                        finish();
                        Paper.book().destroy();
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        getUserStudent(new UserStudentCallback() {
            @Override
            public void onCallback(UserStudent s) {
                student = s;
                welcometext.setText(student.getNickName());
                setProfileImage(student.getProfileuri());
            }
        });

        Intent intent= getIntent();
        uname = intent.getStringExtra("uname");

        storageReference = FirebaseStorage.getInstance().getReference("images/"+uname);



        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent= new Intent(StudentPageActivity.this, CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });



        mysub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,MySubjects.class);
                startActivity(manageActivityIntent);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,AttendanceStud.class);
                startActivity(manageActivityIntent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this, ScheduleStud.class);
                startActivity(manageActivityIntent);
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,HomeworkStud.class);
                startActivity(manageActivityIntent);
            }
        });

        studyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,StudyMaterialStud.class);
                startActivity(manageActivityIntent);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this, NotesActivity.class);
                manageActivityIntent.putExtra("uname",uname);
                startActivity(manageActivityIntent);
            }
        });

        exam_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,ExamsResultsStud.class);
                startActivity(manageActivityIntent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,ReportStud.class);
                startActivity(manageActivityIntent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,UpdateStud.class);
                startActivity(manageActivityIntent);
            }
        });

        txt_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(StudentPageActivity.this,TextRecognitionStud.class);
                startActivity(manageActivityIntent);
            }
        });



        profileEditStd = (ImageView) findViewById(R.id.profileEditStd);
        dialog = new Dialog(this);

        profileEditStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfileEdit();
                Intent activity_pop_upIntent = new Intent(StudentPageActivity.this, PopUp.class);
//                startActivity(activity_pop_upIntent); // studentpage owns activity_pop_up, codes under popup should not work.
            }
        });

    }

    private void openChangePassword() {
        dialog.setContentView(R.layout.change_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        currentPwd = dialog.findViewById(R.id.currentpwd);
        newPwd = dialog.findViewById(R.id.newpwd);
        confirmNewPwd = dialog.findViewById(R.id.confirmnewpwd);
        savePwd = dialog.findViewById(R.id.buttonSavePwd);
        cancelPwd = dialog.findViewById(R.id.buttonCancelPwd);

        newPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (event.getRawX() >= (newPwd.getRight()-
                            newPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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


        savePwd.setOnClickListener(new View.OnClickListener() {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            @Override
            public void onClick(View v) {
                String currentPwdTxt = currentPwd.getText().toString();
                String newPwdTxt = newPwd.getText().toString();
                String confirmNewPwdTxt = confirmNewPwd.getText().toString();

                if (currentPwdTxt.equals("") || newPwdTxt.equals("") || confirmNewPwdTxt.equals("")){
                    TastyToast.makeText(StudentPageActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }else {
                    try {
                        currentOutputPassword = encrypt(currentPwdTxt, currentPwdTxt);
                        currentEncryptedPassword = currentOutputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        newOutputPassword = encrypt(newPwdTxt, newPwdTxt);
                        newEncryptedPassword = newOutputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    verifyCurrentPwd(currentEncryptedPassword, newPwdTxt, confirmNewPwdTxt);

//                    databaseReference.child("students").child(uname).child("password").setValue(newEncryptedPassword);
//                    TastyToast.makeText(StudentPageActivity.this, "Password Changed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                    dialog.dismiss();
                }
            }
        });

        cancelPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(StudentPageActivity.this, "Nothing Changed", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            }

        });
        dialog.show();
    }

    private String encrypt(String data, String pword) throws Exception {
        SecretKeySpec key = generateKey(pword);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String pword) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = pword.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }


    private void verifyCurrentPwd(String currentEncryptedPassword, String newPwdTxt, String confirmNewPwdTxt) {
        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uname)){
                    String dbPassword = snapshot.child(uname).child("password").getValue(String.class);
                    if (!dbPassword.equals(currentEncryptedPassword)){
                        TastyToast.makeText(StudentPageActivity.this, "Current password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!newPwdTxt.matches(passwordPattern)){
                        TastyToast.makeText(StudentPageActivity.this, "Please follow password pattern to make a strong password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!newPwdTxt.equals(confirmNewPwdTxt)){
                        TastyToast.makeText(StudentPageActivity.this, "Confirm password should match with new password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                    else if (newPwdTxt.equals(confirmNewPwdTxt)){
                        updatePassword(newEncryptedPassword);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updatePassword(String newEncryptedPassword) {
        databaseReference.child("students").child(uname).child("password").setValue(newEncryptedPassword);
        TastyToast.makeText(this, "Password Changed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        dialog.dismiss();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }


    private void openProfileEdit() {
        dialog.setContentView(R.layout.activity_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressBar = dialog.findViewById(R.id.progressBarPop);
        etDate = dialog.findViewById(R.id.dob1);
        fullName = dialog.findViewById(R.id.txtFullName);
        nickName = dialog.findViewById(R.id.txtNickName);
        contactNo = dialog.findViewById(R.id.txtContact);
        emailID = dialog.findViewById(R.id.txtEmail);
        address = dialog.findViewById(R.id.txtAddress);
        cropView= dialog.findViewById(R.id.cropView);
        profilepic = dialog.findViewById(R.id.profileEditStd);

        etDate = dialog.findViewById(R.id.dob1);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(student!=null){

            fullName.setText(student.getFullName());
            nickName.setText(student.getNickName());
            etDate.setText(student.getDob());
            contactNo.setText(student.getContactNo());
            emailID.setText(student.getEmail());
            address.setText(student.getAddress());

            if(!student.getProfileuri().equals(""))
                Glide.with(this).load(student.getProfileuri()).into(cropView);
        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        StudentPageActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                etDate.setText(date);
            }
        };


        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(v.getContext(), "Nothing changed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

            }
        });

        Button buttonSave = dialog.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(v.getContext(), "Saved changes", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                String fName = fullName.getText().toString();
                String nName = nickName.getText().toString();
                String dob = etDate.getText().toString();
                String cNumber = contactNo.getText().toString();
                String eID = emailID.getText().toString();
                String adrs = address.getText().toString();
//                Uri profilrUri = dwnUri;


                welcometext.setText(nickName.getText().toString());

                UserStudent s = new UserStudent(fName,nName,dob,cNumber,eID,adrs,"");


                if (imageUri != null) {
                    uploadToFirebase(imageUri,s);
                }
                else {
                    uploadStudent(s);
                }


                getUserStudent(new UserStudentCallback() {
                    @Override
                    public void onCallback(UserStudent s) {
                        student = s;
                    }
                });

//                getUserStudent(new UserStudentCallback() {
//                    @Override
//                    public void onCallback(UserStudentCallback studentCallback) {
//
//                    }
//
//                    @Override
//                    public void onCallback(UserStudent studentCallback) {
//                        student = studentCallback;
//                    }
//                });

            }
        });

        cropView= dialog.findViewById(R.id.cropView);

        cropView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });


        dialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==-1 && requestCode==101)
        {
            String result=data.getStringExtra("RESULT");
            Uri resultUri=null;
            if (result!=null)
            {
                resultUri=Uri.parse(result);
            }

            cropView.setImageURI(resultUri);
        }
    }


    private void uploadToFirebase(Uri imageUri, UserStudent s) {
            final StorageReference fileRef = storageReference.child(uname + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri imageuri) {
                            s.setProfileuri(imageuri.toString());
                            TastyToast.makeText(StudentPageActivity.this, "Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

//                        cropView.setImageResource(R.drawable.img);
                            setProfileImage(imageUri.toString());
                            uploadStudent(s);
                            getUserStudent(new UserStudentCallback() {
                                @Override
                                public void onCallback(UserStudent s) {
                                    student = s;
                                }
                            });

                        }
                    });
                }
            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
                    TastyToast.makeText(StudentPageActivity.this, "Uploading Failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }
            });


    }


    private void getUserStudent(UserStudentCallback callback){

        databaseReference.child("studentsAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uname)){
                    UserStudent value = snapshot.child(uname).getValue(UserStudent.class);
                    callback.onCallback(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private String getFileExtension(Uri muri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }

    private void setProfileImage(String uri){
        Glide.with(this).load(uri).into(profileEditStd);
    }

    private void uploadStudent(UserStudent s){
        databaseReference.child("studentsAccount").child(uname).setValue(s);
        progressBar.setVisibility(View.GONE);
        TastyToast.makeText(StudentPageActivity.this, "Saved changes", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        dialog.dismiss();
    }

}
