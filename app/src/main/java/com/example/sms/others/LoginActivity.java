package com.example.sms.others;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sms.R;
import com.example.sms.admin.AdminFingerPrintAuth;
import com.example.sms.admin.TeacherPageActivity;
import com.example.sms.students.StudentFingerPrintAuth;
import com.example.sms.students.StudentPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.paperdb.Paper;


public class LoginActivity extends AppCompatActivity {

    //DatabaseReference databaseReference;

    String outputPassword;
    String encryptedPassword;
    String AES = "AES";
    TextView forgotPassword;
    Dialog dialog;
    String newOutputPassword;
    String newEncryptedPassword;
    String dbChild;
    String username;

    public static ProgressBar progressBarOfLogin;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://asms-365d0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        EditText username = findViewById(R.id.txtTeaUserName);
        TextView password = findViewById(R.id.txtTeaPassWord);
        Button login_btn = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBarOfLogin = findViewById(R.id.progressBarOfTeacherLogin);
        dialog = new Dialog(this);

        Paper.init(LoginActivity.this);
        String UserNameKey = Paper.book().read(OnlineUsers.UserNamekey);
        String UserPasswordKey = Paper.book().read(OnlineUsers.UserPasswordKey);

        if (UserNameKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                autoLogin(UserNameKey, UserPasswordKey);
                progressBarOfLogin.setVisibility(View.VISIBLE);
            }
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pword = password.getText().toString();

                if (uname.equals("") || pword.equals("")) {
                    TastyToast.makeText(v.getContext(), "Username or Password is empty", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                } else {
                    try {
                        outputPassword = encrypt(pword, pword);
                        encryptedPassword = outputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginUser(uname, encryptedPassword);
                    progressBarOfLogin.setVisibility(View.VISIBLE);
//                 finish();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenForgotPassword();
            }
        });

    }

    private void OpenForgotPassword() {
        dialog.setContentView(R.layout.forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout reset_password_layout = dialog.findViewById(R.id.reset_password_layout);
        LinearLayout validate_user_layout = dialog.findViewById(R.id.validate_user_layout);
        Button validateUser = dialog.findViewById(R.id.validateUser);
        Button buttonSavePwd_fp = dialog.findViewById(R.id.buttonSavePwd_fp);
        Button buttonCancelPwd_fp = dialog.findViewById(R.id.buttonCancelPwd_fp);
        EditText uname_validate = dialog.findViewById(R.id.uname_validate);
        EditText email_validate = dialog.findViewById(R.id.email_validate);
        EditText newpwd_fp = dialog.findViewById(R.id.newpwd_fp);
        EditText confirmnewpwd_fp = dialog.findViewById(R.id.confirmnewpwd_fp);

        newpwd_fp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (newpwd_fp.getRight() -
                            newpwd_fp.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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

        validateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarOfLogin.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference1.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        username = uname_validate.getText().toString();
                        String email = email_validate.getText().toString();
                        if (username.isEmpty() || email.isEmpty()) {
                            TastyToast.makeText(LoginActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                            progressBarOfLogin.setVisibility(View.INVISIBLE);
                        } else if (snapshot.hasChild(username)) {
                            String dbEmail = snapshot.child(username).child("email").getValue(String.class);
                            int x = 1;
                            if (dbEmail.equals(email)) {
                                TastyToast.makeText(LoginActivity.this, "User is validated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                validate_user_layout.setVisibility(View.GONE);
                                reset_password_layout.setVisibility(View.VISIBLE);
                                progressBarOfLogin.setVisibility(View.INVISIBLE);
                                dbChild = "users";
                            } else {
                                TastyToast.makeText(LoginActivity.this, "Validation failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                progressBarOfLogin.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            databaseReference1.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(username)) {
                                        String dbEmail = snapshot.child(username).child("email").getValue(String.class);
                                        if (dbEmail.equals(email)) {
                                            TastyToast.makeText(LoginActivity.this, "User is validated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            validate_user_layout.setVisibility(View.GONE);
                                            reset_password_layout.setVisibility(View.VISIBLE);
                                            progressBarOfLogin.setVisibility(View.INVISIBLE);
                                            dbChild = "students";
                                        } else
                                            TastyToast.makeText(LoginActivity.this, "Validation failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                        progressBarOfLogin.setVisibility(View.GONE);
                                    } else
                                        TastyToast.makeText(LoginActivity.this, "Validation failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressBarOfLogin.setVisibility(View.GONE);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBarOfLogin.setVisibility(View.GONE);
                    }
                });
            }
        });

        buttonSavePwd_fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarOfLogin.setVisibility(View.VISIBLE);
                String newPassword = newpwd_fp.getText().toString();
                String confirmNewPassword = confirmnewpwd_fp.getText().toString();

                if (newPassword.equals("") || confirmNewPassword.equals("")) {
                    TastyToast.makeText(LoginActivity.this, "Please fill all fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    progressBarOfLogin.setVisibility(View.GONE);
                } else if (!newPassword.equals(confirmNewPassword)) {
                    TastyToast.makeText(LoginActivity.this, "Passwords Mismatch", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    progressBarOfLogin.setVisibility(View.GONE);
                } else if (newPassword.equals(confirmNewPassword)) {
                    try {
                        newOutputPassword = encrypt(newPassword, newPassword);
                        newEncryptedPassword = newOutputPassword;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    updatePassword(newEncryptedPassword);
                }
            }
        });

        buttonCancelPwd_fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updatePassword(String newEncryptedPassword) {
        databaseReference.child(dbChild).child(username).child("password").setValue(newEncryptedPassword);
        TastyToast.makeText(this, "Password Reset Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        progressBarOfLogin.setVisibility(View.GONE);
        dialog.dismiss();
    }

    private void autoLogin(String userNameKey, String userPasswordKey) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userNameKey)) {
                    String dbPassword = snapshot.child(userNameKey).child("password").getValue(String.class);

                    if (dbPassword.equals(userPasswordKey)) {
                        Intent loginIntent = new Intent(LoginActivity.this, AdminFingerPrintAuth.class);
                        progressBarOfLogin.setVisibility(View.INVISIBLE);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        TastyToast.makeText(LoginActivity.this, "It seems you have changed your password. Please login with your new password.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        progressBarOfLogin.setVisibility(View.INVISIBLE);
                    }
                } else {
                    databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(userNameKey)) {
                                String dbPassword = snapshot.child(userNameKey).child("password").getValue(String.class);

                                if (dbPassword.equals(userPasswordKey)) {
                                    Intent studentLoginIntent = new Intent(LoginActivity.this, StudentFingerPrintAuth.class);
                                    studentLoginIntent.putExtra("uname", userNameKey);
                                    progressBarOfLogin.setVisibility(View.INVISIBLE);
                                    startActivity(studentLoginIntent);


                                } else {
                                    TastyToast.makeText(LoginActivity.this, "It seems you have changed your password. Please login with your new password.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                    progressBarOfLogin.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                progressBarOfLogin.setVisibility(View.INVISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            TastyToast.makeText(LoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            progressBarOfLogin.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(LoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                progressBarOfLogin.setVisibility(View.INVISIBLE);
            }
        });

    }

    private String encrypt(String data, String password) throws Exception {
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

    private void loginUser(String uname, String pword) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uname)) {
                    String dbPassword = snapshot.child(uname).child("password").getValue(String.class);

                    if (dbPassword.equals(pword)) {
                        TastyToast.makeText(LoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent teacherLoginIntent = new Intent(LoginActivity.this, TeacherPageActivity.class);
                        Paper.book().write(OnlineUsers.UserNamekey, uname);
                        Paper.book().write(OnlineUsers.UserPasswordKey, pword);
                        progressBarOfLogin.setVisibility(View.INVISIBLE);
                        startActivity(teacherLoginIntent);
                        finish();
                    } else {
                        TastyToast.makeText(LoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        progressBarOfLogin.setVisibility(View.INVISIBLE);
                    }
                } else {
                    databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(uname)) {
                                String dbPassword = snapshot.child(uname).child("password").getValue(String.class);

                                if (dbPassword.equals(pword)) {
                                    TastyToast.makeText(LoginActivity.this, "Login successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    Intent studentLoginIntent = new Intent(LoginActivity.this, StudentPageActivity.class);
                                    Paper.book().write(OnlineUsers.UserNamekey, uname);
                                    Paper.book().write(OnlineUsers.UserPasswordKey, pword);
                                    startActivity(studentLoginIntent);


                                } else {
                                    TastyToast.makeText(LoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            } else {
                                TastyToast.makeText(LoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                            progressBarOfLogin.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    TastyToast.makeText(LoginActivity.this, "Username or Password is wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                    progressBarOfTeacherLogin.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TastyToast.makeText(LoginActivity.this, "Connection failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                progressBarOfLogin.setVisibility(View.GONE);
            }
        });


    }


}
