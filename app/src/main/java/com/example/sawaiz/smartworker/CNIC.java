package com.example.sawaiz.smartworker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

public class CNIC extends AppCompatActivity {

    private Button  backBtn, resetBtn, saveBTn;
    private ImageView backImg;
    private ProgressBar progressBar;
    private static final int TAKE_IMAGE = 0;
    private static final int PERMISSION_CODE = 1000;

    private boolean SwitchingButton1 = false;
    Uri Image_uri;
    private Uri UriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnic);


        backImg = (ImageView)(findViewById(R.id.CNICback_img));
        backBtn = (Button)(findViewById(R.id.backimage_btn));
        resetBtn = (Button)(findViewById(R.id.cnicReset_btn));
        saveBTn = (Button)(findViewById(R.id.cnicSave_Btn));
        progressBar = (ProgressBar)(findViewById(R.id.cnicBar));



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission is already granted

                        openCamera();

                    }

                } else {

                    //system os < marshmallow

                    openCamera();
                }

            }
            });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        saveBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingCNIC();
                Intent loginIntent = new Intent(CNIC.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }


    private void openCamera()
    {
        Intent takeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takeImage, TAKE_IMAGE);

        resetBtn.setVisibility(View.VISIBLE);
        saveBTn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {


                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }


        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_IMAGE) {
            final Uri imageUri = data.getData();
            UriImage = imageUri;
            backImg.setImageURI(UriImage);
        }
    }


    public void savingCNIC()
    {
        if (UriImage != null) {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String user_id = auth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference().child("Users").child("Handyman").child(user_id);

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("CNIC").child(user_id);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), UriImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    Map newImage = new HashMap();
                    newImage.put("CNICImageURL", downloadUrl);
                    myRef.updateChildren(newImage);

                    finish();
                    return;
                }
            });
        } else {
            finish();
        }


    }


}
