package com.example.sawaiz.smartworker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CNIC extends AppCompatActivity {

    private Button  backBtn, resetBtn, saveBTn;
    private ImageView backImg;

    private static final int TAKE_IMAGE = 0;
    private static final int PERMISSION_CODE = 1000;
    private Uri contentURI;
    ProgressDialog progressDialog;
    String downloadUri;
    private static final String IMAGE_DIRECTORY = "/CNIC";
    private FirebaseAuth mAuth;
    String userID;
    private DatabaseReference myRef, reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnic);

        progressDialog = new ProgressDialog (this);

        userID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userID);



        backImg = (ImageView)(findViewById(R.id.CNICback_img));
        backBtn = (Button)(findViewById(R.id.backimage_btn));
        resetBtn = (Button)(findViewById(R.id.cnicReset_btn));
        saveBTn = (Button)(findViewById(R.id.cnicSave_Btn));

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
                uploadImage();
                Toast.makeText(getApplicationContext(), "CNIC Uploaded Successfully",
                        Toast.LENGTH_SHORT).show();

                Intent loginIntent = new Intent(CNIC.this, LoginActivity.class);
                startActivity(loginIntent);
                Toast.makeText(getApplicationContext(),"Congratulations! Your Account has been Created. Please Login into your account.",Toast.LENGTH_SHORT).show();
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
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            backImg.setImageBitmap(thumbnail);
            savingCNIC(thumbnail);
        }
    }


    public String savingCNIC(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getFileExtenstion(Uri uri) {
        ContentResolver cr = Objects.requireNonNull(getApplicationContext()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImage() {

        progressDialog.setMessage("Uploading CNIC...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (contentURI != null) {

            final StorageReference filReference = FirebaseStorage.getInstance().getReference().child("CNIC").child(userID);

            final UploadTask uploadTask = filReference.putFile(contentURI);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload failed.",
                            Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Upload failed.",
                                        Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                                throw task.getException();
                            }
                            downloadUri = filReference.getDownloadUrl().toString();
                            return filReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUri = task.getResult().toString();
                                Map newImage = new HashMap();
                                newImage.put("CNICImageURL", downloadUri);
                                myRef.updateChildren(newImage);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "CNIC Uploaded Successfully",
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
            });

        }
        else {
            return;
        }


    }
}
