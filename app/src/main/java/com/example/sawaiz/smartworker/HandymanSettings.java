package com.example.sawaiz.smartworker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HandymanSettings extends AppCompatActivity {

    private EditText fname, lname;
    private TextView email, phone, cnic;

    private Button backBtn, confirmBtn, updatePassBtn, profilePhotoBtn,selectPhoto;

    private ImageView profileImageView;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef, reference;

    private String userID;
    private String FName;
    private String Phone;
    private String LName;
    private String Email;
    private String CNIC;
    private String HandymanSkill;
    private String profileImageURL;
    private RadioGroup HandymanRadioGroup;

    private int GALLERYIMAGE1 = 1;
    private int CAMERA1 = 11;
    Uri contentURI;
    private static final String IMAGE_DIRECTORY = "/ProfilePhoto";
    String downloadUri;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handyman_settings);

        progressDialog = new ProgressDialog (this);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) (findViewById(R.id.email));
        cnic = (TextView) (findViewById(R.id.cnic));

        profileImageView = (ImageView) findViewById(R.id.profileImage);

        backBtn = (Button) findViewById(R.id.back);
        confirmBtn = (Button) findViewById(R.id.confirm);
        updatePassBtn = (Button) (findViewById(R.id.updatePasswordBtn));
        profilePhotoBtn = (Button) (findViewById(R.id.profileImageBtn));
        selectPhoto = (Button)(findViewById(R.id.SelectprofileImageBtn));

        HandymanRadioGroup = (RadioGroup) (findViewById(R.id.radioGroupBtnSkills));

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userID);

        getUserInfo();

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERYIMAGE1);
            }
        });


        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HandymanSettings.this, fogotpassword.class);
                startActivity(i);
                return;
            }
        });

        profilePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadImage();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHandymanInformation();
                Toast.makeText(getApplicationContext(), "Changes Saved Successfully.", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("FirstName") != null) {
                        FName = map.get("FirstName").toString();
                        fname.setText(FName);
                    }
                    if (map.get("LastName") != null) {
                        LName = map.get("LastName").toString();
                        lname.setText(LName);
                    }
                    if (map.get("PhoneNumber") != null) {
                        Phone = map.get("PhoneNumber").toString();
                        phone.setText(Phone);
                    }
                    if (map.get("EmailAddress") != null) {
                        Email = map.get("EmailAddress").toString();
                        email.setText(Email);
                    }
                    if (map.get("CNIC") != null) {
                        CNIC = map.get("CNIC").toString();
                        cnic.setText(CNIC);
                    }

                    if (map.get("Skill") != null) {
                        HandymanSkill = map.get("Skill").toString();
                        switch (HandymanSkill) {
                            case "Plumber":
                                HandymanRadioGroup.check(R.id.sPlumber);
                                break;
                            case "Electrician":
                                HandymanRadioGroup.check(R.id.sElectrician);
                                break;
                            case "Gardener":
                                HandymanRadioGroup.check(R.id.sGardener);
                                break;
                            case "HouseKeeper":
                                HandymanRadioGroup.check(R.id.sHouseKeeper);
                                break;
                            case "Carpenter":
                                HandymanRadioGroup.check(R.id.sCarpenter);
                                break;
                            case "Painter":
                                HandymanRadioGroup.check(R.id.sPainter);
                                break;
                            case "Mason":
                                HandymanRadioGroup.check(R.id.sMason);
                                break;
                            case "ApplianceRepairer":
                                HandymanRadioGroup.check(R.id.sApplianceRepairer);
                                break;
                        }
                    }
                    if (map.get("profileImageUrl") != null) {
                        profileImageURL = map.get("profileImageUrl").toString();
                            Picasso.get().load(profileImageURL).fit().into(profileImageView);
                            //Toast.makeText(getApplicationContext(),profileImageURL,Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void saveHandymanInformation() {

        int HandymanRadioBtnID = HandymanRadioGroup.getCheckedRadioButtonId();

        final RadioButton handymanUpdateSkillRadioBtn = (RadioButton) findViewById(HandymanRadioBtnID);

        if (handymanUpdateSkillRadioBtn.getText() == null) {
            return;
        }

        HandymanSkill = handymanUpdateSkillRadioBtn.getText().toString();


        Map SavingUser = new HashMap();
        SavingUser.put("FirstName", fname.getText().toString());
        SavingUser.put("LastName", lname.getText().toString());
        SavingUser.put("Skill", HandymanSkill);
        myRef.updateChildren(SavingUser);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERYIMAGE1) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getApplication()).getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    profileImageView.setImageBitmap(bitmap);
                    profilePhotoBtn.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else{
            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
        }
    }


    public String saveImage(Bitmap myBitmap) {
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

        progressDialog.setMessage("Updating Photo...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (contentURI != null) {

            final StorageReference filReference = FirebaseStorage.getInstance().getReference().child("ProfilePhoto").child(userID);

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
                                newImage.put("profileImageUrl", downloadUri);
                                myRef.updateChildren(newImage);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully",
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

