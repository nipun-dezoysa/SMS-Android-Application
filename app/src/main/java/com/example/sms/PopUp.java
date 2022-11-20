package com.example.sms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PopUp extends AppCompatActivity {

    ImageView cropView;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

//        TextView fname = findViewById(R.id.txtFullName);
//        fname.setText("Is this working?");
//        cropView= findViewById(R.id.cropView);
//
//        cropView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mGetContent.launch("image/*");
//            }
//        });
//
//        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                Intent intent= new Intent(PopUp.this,CropperActivity.class);
//                intent.putExtra("DATA",result.toString());
//                startActivityForResult(intent,101);
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==-1 && requestCode==101)
//        {
//            String result=data.getStringExtra("RESULT");
//            Uri resultUri=null;
//            if (result!=null)
//            {
//                resultUri=Uri.parse(result);
//            }
//
//            cropView.setImageURI(resultUri);
//        }
//    }

}



//    private CircleImageView profileImageView;
//    private Button cancelButton, saveButton;
//    private TextView profileChangeBtn;
//
//    private DatabaseReference databaseReference;
//    private FirebaseAuth mAuth;
//
//    private Uri imageUri;
//    private String myUri = "";
//    private StorageTask UploadTask;
//    private StorageReference storageProfilePicsRef;


//    private void uploadProfileImage() {
//    }


//
//
////        init
////
//        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
//        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
//
//        profileImageView = findViewById(R.id.profile_image);
//
//        cancelButton = findViewById(R.id.buttonCancel);
//        saveButton = findViewById(R.id.buttonSave);
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PopUp.this, TeacherPageActivity.class));
//            }
//        });
////
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                uploadProfileImage();
//            }
//        });
//        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
////            }
//        });
//
//


