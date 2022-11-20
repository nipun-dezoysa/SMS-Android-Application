package com.example.sms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Test extends AppCompatActivity {


//    Button btChoose,btUpload;
//    EditText etName;
//    ImageView ivResult;
//    TextView tvUrl;
//
//    private static final int Selected = 100;
//    Uri uriImage;
//
//    FirebaseStorage storage;
//    StorageReference storageRef,imageRef;
//    ProgressDialog progressDialog;
//    UploadTask uploadTask;

//    private int CAMERA_PIC_REQUEST=100;
//    private int GALLARY_PIC_REQUEST=200;
//    Uri imageUri;
//    ImageView imageFromGallary,imageFromDatabase=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
////
//        imageFromGallary=findViewById(R.id.my_image);
//        imageFromDatabase=findViewById(R.id.image_from_database);

//        retrieveData();
//        btChoose = findViewById(R.id.bt_choose);
//        btUpload = findViewById(R.id.bt_upload);
//        etName = findViewById(R.id.et_name);
//        ivResult = findViewById(R.id.iv_result);
//        tvUrl = findViewById(R.id.tv_url);
//
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
//
//        btChoose.setOnClickListener((v) ->{
//            Intent photopicker = new Intent(Intent.ACTION_PICK);
//            photopicker.setType("image/*");
//            startActivityForResult(photopicker,Selected);
//        } );
//
//        etName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                btUpload.setEnabled(true);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        btUpload.setOnClickListener((v) -> { UploadImage(); });
    }

//    private void retrieveData() {
//        DatabaseReference db= FirebaseDatabase.getInstance().getReference("riddle_game")
//                .child("myImages").child("user1").child("newImage");
//        db.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    Picasso.with(Test.this).load(Uri.parse(snapshot.getValue().toString())).into(imageFromDatabase);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Selected:
//                if (resultCode == RESULT_OK){
//                    assert data != null;
//                    uriImage = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
//                                uriImage);
//                        ivResult.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
//    }
//
//    private void UploadImage() {
//        imageRef = storageRef.child("Images/"+etName.getText().toString()+"."+
//                GetExtension(uriImage));
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMax(100);
//        progressDialog.setMessage("Uploading...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.show();
//        progressDialog.setCancelable(false);
//
//        uploadTask = imageRef.putFile(uriImage);
//
//        uploadTask.addOnProgressListener((OnProgressListener<? super UploadTask.TaskSnapshot>)(
//        double progress = (100.0 * taskSnapshot.get)
//                )
//
//                );
//    }
//
//    private String GetExtension(Uri uriImage) {
//    }
}