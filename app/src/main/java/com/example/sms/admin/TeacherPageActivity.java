package com.example.sms.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.sms.others.CropperActivity;
import com.example.sms.others.LoginActivity;
import com.example.sms.others.NotesActivity;
import com.example.sms.R;
import com.example.sms.others.AddPhotos;
import com.example.sms.interfaces.TeacherCallback;
import com.example.sms.model.Teacher;
import com.example.sms.others.OnlineUsers;
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
import com.google.firebase.storage.StorageTask;
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

public class TeacherPageActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    ProgressBar progressBar;
    ProgressBar progressBarChangepwd;

    String currentOutputPassword;
    String currentEncryptedPassword;
    String AES = "AES";
    String newOutputPassword;
    String newEncryptedPassword;

    EditText currentPwd;
    EditText newPwd;
    EditText confirmNewPwd;
    Button savePwd;
    Button cancelPwd;

    Teacher teacher;
    String uname;

    StorageReference storageReference;
    DatabaseReference imageDBReference = FirebaseDatabase.getInstance().getReference("Image");

    TextView etDate;
    DatePickerDialog.OnDateSetListener setListener;

    EditText fullName;
    EditText nickName;
    EditText contactNo;
    EditText emailID;
    EditText address;
    ImageView profileEdit;
    Uri imageUri;
    Uri dwnUri;
    String myUri = "";
    StorageTask uploadTask;
    StorageReference storageProfilePicsRef;
    Uri uploadUri;
    Dialog dialog;

    CircleImageView profilepic;
    CircleImageView cropView;
    ActivityResultLauncher<String> mGetContent;
    TextView cam_uri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    ImageView adminmenupopupbutton;

    TextView welcometext;
    TextView date;
    TextView time;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherpage);

        date = findViewById(R.id.current_date);
        time = findViewById(R.id.current_time);
        profileEdit = findViewById(R.id.profileEdit);
        welcometext = findViewById(R.id.dashboardtxt);
        date.setText(getCurrentDate());
        time.setText(getCurrentTime());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        adminmenupopupbutton = findViewById(R.id.adminmenupopupbutton);

        adminmenupopupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Logout")
                                .setMessage("Are you sure you want to Logout ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Paper.book().destroy();
                                        TastyToast.makeText(v.getContext(), " Logged out successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                        Intent logoutIntent = new Intent(v.getContext(), LoginActivity.class);
                                        v.getContext().startActivity(logoutIntent);
                                        LoginActivity.progressBarOfLogin.setVisibility(View.INVISIBLE);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

        });

        getTeacher(new TeacherCallback() {
            @Override
            public void onCallback(Teacher t) {

                teacher = t;
                welcometext.setText(teacher.getNickName());
                setProfileImage(teacher.getProfileuri());
            }
        });

        Paper.init(TeacherPageActivity.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);


        storageReference = FirebaseStorage.getInstance().getReference("images/" + uname);


        LinearLayout reg_std = findViewById(R.id.mng_std);
        LinearLayout attendance = findViewById(R.id.atn_id);
        LinearLayout schedule = findViewById(R.id.schedule_id);
        LinearLayout homework = findViewById(R.id.homeworkid);
        LinearLayout studyMaterial = findViewById(R.id.study_mat_id);
        LinearLayout notes = findViewById(R.id.notes_id);
        LinearLayout exam_result = findViewById(R.id.exams_results);
        LinearLayout report = findViewById(R.id.report_id);
        LinearLayout update = findViewById(R.id.update_id);
        LinearLayout txt_rec = findViewById(R.id.txt_rec);
        LinearLayout dashboard = findViewById(R.id.dashboard);


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(TeacherPageActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });


        reg_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, Manage_User.class);
                startActivity(manageActivityIntent);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, AttendanceActivity.class);
                startActivity(manageActivityIntent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, ScheduleActivity.class);
                startActivity(manageActivityIntent);
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, Home_Work.class);
                startActivity(manageActivityIntent);
            }
        });

        studyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, StudyMaterial.class);
                startActivity(manageActivityIntent);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, NotesActivity.class);
                startActivity(manageActivityIntent);
            }
        });

        exam_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, ResultsHome.class);
                startActivity(manageActivityIntent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, ReportActivity.class);
                startActivity(manageActivityIntent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, UpdateActivity.class);
                startActivity(manageActivityIntent);
            }
        });

        txt_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageActivityIntent = new Intent(TeacherPageActivity.this, TextRecognition.class);
                startActivity(manageActivityIntent);
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TeacherPageActivity.this, Dashboard.class));
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://console.firebase.google.com/project/asms-365d0/analytics/app/android:com.example.sms/overview/~2F%3Ft%3D1673185323615&fpn%3D566350586702&swu%3D1&sgu%3D1&sus%3Dupgraded&cs%3Dapp.m.dashboard.overview&g%3D1"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // if Chrome browser not installed, allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });


        dialog = new Dialog(this);

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfileEdit();
//                Intent activity_pop_upIntent = new Intent(TeacherPageActivity.this,PopUp.class);
//                startActivity(activity_pop_upIntent); // teacherpage owns activity_pop_up, codes under popup should not work.
            }
        });


    } //on create close

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

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (newPwd.getRight() -
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

                if (currentPwdTxt.equals("") || newPwdTxt.equals("") || confirmNewPwdTxt.equals("")) {
                    TastyToast.makeText(TeacherPageActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
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
//                    verifyCurrentPwd(currentPwdTxt, newPwdTxt, confirmNewPwdTxt);
                }
            }
        });

        cancelPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(TeacherPageActivity.this, "Nothing Changed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            final String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uname)) {
                    String dbPassword = snapshot.child(uname).child("password").getValue(String.class);
                    if (!dbPassword.equals(currentEncryptedPassword)) {
                        TastyToast.makeText(TeacherPageActivity.this, "Current password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!newPwdTxt.matches(passwordPattern)) {
                        TastyToast.makeText(TeacherPageActivity.this, "Please follow password pattern to make a strong password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!newPwdTxt.equals(confirmNewPwdTxt)) {
                        TastyToast.makeText(TeacherPageActivity.this, "Confirm password should match with new password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (newPwdTxt.equals(confirmNewPwdTxt)) {
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
        databaseReference.child("users").child(uname).child("password").setValue(newEncryptedPassword);
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
        cropView = dialog.findViewById(R.id.cropView);
        profilepic = dialog.findViewById(R.id.profileEdit);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (teacher != null) {
            fullName.setText(teacher.getFullName().trim());
            nickName.setText(teacher.getNickName().trim());
            etDate.setText(teacher.getDob());
            contactNo.setText(teacher.getContactNo().trim());
            emailID.setText(teacher.getEmail().trim());
            address.setText(teacher.getAddress().trim());


            if (!teacher.getProfileuri().equals(""))
                Glide.with(this).load(teacher.getProfileuri()).into(cropView);

        }


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TeacherPageActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etDate.setText(date);
            }
        };


        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TastyToast.makeText(TeacherPageActivity.this, "Nothing changed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);


            }
        });

        Button buttonSave = dialog.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String fName = fullName.getText().toString();
                String nName = nickName.getText().toString();
                String dob = etDate.getText().toString();
                String cNumber = contactNo.getText().toString();
                String eID = emailID.getText().toString();
                String adrs = address.getText().toString();


                welcometext.setText(nickName.getText().toString());


                Teacher t = new Teacher("", fName, nName, dob, cNumber, eID, adrs);

                if (imageUri != null) {
                    uploadToFirebase(imageUri, t);
                } else {
                    uploadTeacher(t);
                }

                getTeacher(new TeacherCallback() {
                    @Override
                    public void onCallback(Teacher t) {
//
                        setProfileImage(teacher.getProfileuri());
                        teacher = t;


                    }
                });

            }
        });


        cropView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TeacherPageActivity.this, "ImageView is working", Toast.LENGTH_SHORT).show();
                mGetContent.launch("image/*");
            }
        });

//        getUserinfo();

        dialog.show();


    } //openProfileEdit close


//    private void getUserinfo() {
//        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists() && snapshot.getChildrenCount() > 0 )
//                {
//                    if (snapshot.hasChild("image"))
//                    {
//                        String image = snapshot.child("image").getValue().toString();
//                        Picasso.get().load(image).into(profilepic);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void uploadProfileImage() {
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Set your profile");
//        progressDialog.setMessage("Uploading");
//        progressDialog.show();
//
//        if (imageUri != null){
//
//            final StorageReference fileRef = storageProfilePicsRef
//                    .child(mAuth.getCurrentUser().getUid()+".jpg");
//
//
//            uploadTask.continueWithTask(new Continuation() {
//                @Override
//                public Object then(@NonNull Task task) throws Exception {
//
//                    if (!task.isSuccessful())
//                    {
//                        throw task.getException();
//                    }
//                    return fileRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful())
//                    {
//                        Uri downloadUrl =task.getResult();
//                        myUri = downloadUrl.toString();
//
//                        HashMap<String, Object> userMap = new HashMap<>();
//                        userMap.put("image",myUri);
//
//                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
//
//                        progressDialog.dismiss();
//                    }
//
//                }
//            });
//
//        }
//
//        else {
//            progressDialog.dismiss();
//            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
//        }
//    }

    //    back error start
    private void getTeacher(TeacherCallback callback) {

        databaseReference.child("teachers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uname)) {
                    Teacher value = snapshot.child(uname).getValue(Teacher.class);
                    //Log.i("test",value.fullName);
                    callback.onCallback(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                // System.out.println("unable to connect to db");
            }
        });


    }

    //    back error end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
                imageUri = resultUri;
            }
            cropView.setImageURI(resultUri);
        }
    }

//    private Uri saveImage(Bitmap image, Context context) {
//
//        File imagesFolder=new File(context.getCacheDir(),"images");
//        Uri uri=null;
//        try {
//            imagesFolder.mkdirs();
//            File file=new File(imagesFolder, "captured_image.jpg");
//            FileOutputStream stream=new FileOutputStream(file);
//            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
//            stream.flush();
//            stream.close();
//            uri= FileProvider.getUriForFile(context.getApplicationContext(),"com.example.test"+".provider",file);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return uri;
//    }

    private void uploadToFirebase(Uri uri, Teacher t) {
        final StorageReference fileRef = storageReference.child(uname + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        t.setProfileuri(uri.toString());
                        TastyToast.makeText(TeacherPageActivity.this, "Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

//                        cropView.setImageResource(R.drawable.img);
                        setProfileImage(uri.toString());
                        uploadTeacher(t);
                        getTeacher(new TeacherCallback() {
                            @Override
                            public void onCallback(Teacher t) {
                                teacher = t;
                            }
                        });

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
                TastyToast.makeText(TeacherPageActivity.this, "Uploading Failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

            }
        });

    }

    private void uploadTeacher(Teacher t) {
        databaseReference.child("teachers").child(uname).setValue(t);
        progressBar.setVisibility(View.GONE);
        TastyToast.makeText(TeacherPageActivity.this, "Saved changes", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        dialog.dismiss();
    }

    private String getFileExtension(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void setProfileImage(String uri) {
        Glide.with(this).load(uri).into(profileEdit);
    }

}




