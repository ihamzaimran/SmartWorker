package com.example.sawaiz.smartworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandymanSettings extends AppCompatActivity {

    private EditText fname, lname;
    private TextView email, phone, cnic;

    private Button backBtn, confirmBtn, updatePassBtn,profilePhotoBtn;

    private ImageView profileImageView;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    private String userID;
    private String FName;
    private String Phone;
    private String LName;
    private String Email;
    private String CNIC;
    private String HandymanSkill;
    private String profileImageURL;

    private Uri resultUri;

    private RadioGroup HandymanRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handyman_settings);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) (findViewById(R.id.email));
        cnic = (TextView) (findViewById(R.id.cnic));

        profileImageView = (ImageView) findViewById(R.id.profileImage);

        backBtn = (Button) findViewById(R.id.back);
        confirmBtn = (Button) findViewById(R.id.confirm);
        updatePassBtn = (Button)(findViewById(R.id.updatePasswordBtn));
        profilePhotoBtn = (Button)(findViewById(R.id.profileImageBtn));

        HandymanRadioGroup = (RadioGroup)(findViewById(R.id.radioGroupBtnSkills));

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userID);

        getUserInfo();


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
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
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
    private void getUserInfo(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("FirstName")!=null){
                        FName = map.get("FirstName").toString();
                        fname.setText(FName);
                    }
                    if(map.get("LastName")!=null){
                        LName = map.get("LastName").toString();
                        lname.setText(LName);
                    }
                    if(map.get("PhoneNumber")!=null){
                        Phone = map.get("PhoneNumber").toString();
                        phone.setText(Phone);
                    }
                    if(map.get("EmailAddress")!=null){
                        Email = map.get("EmailAddress").toString();
                        email.setText(Email);
                    }
                    if(map.get("CNIC")!=null){
                        CNIC = map.get("CNIC").toString();
                        cnic.setText(CNIC);
                    }

                    if(map.get("Skill")!=null){
                        HandymanSkill = map.get("Skill").toString();
                        switch (HandymanSkill){
                            case"Plumber":
                                HandymanRadioGroup.check(R.id.sPlumber);
                                break;
                            case"Electrician":
                                HandymanRadioGroup.check(R.id.sElectrician);
                                break;
                            case"Gardener":
                                HandymanRadioGroup.check(R.id.sGardener);
                                break;
                            case"House Keeper":
                                HandymanRadioGroup.check(R.id.sHouseKeeper);
                                break;
                            case"Carpenter":
                                HandymanRadioGroup.check(R.id.sCarpenter);
                                break;
                            case"Painter":
                                HandymanRadioGroup.check(R.id.sPainter);
                                break;
                            case"Mason":
                                HandymanRadioGroup.check(R.id.sMason);
                                break;
                            case"Appliance Repairer":
                                HandymanRadioGroup.check(R.id.sApplianceRepairer);
                                break;

                        }
                    }

                    if(map.get("profileImageUrl")!=null){
                        profileImageURL = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(profileImageURL).into(profileImageView);
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

        if (handymanUpdateSkillRadioBtn.getText() == null){
            return;
        }

        HandymanSkill = handymanUpdateSkillRadioBtn.getText().toString();


        Map SavingUser = new HashMap();
        SavingUser.put("FirstName",fname.getText().toString());
        SavingUser.put("LastName",lname.getText().toString());
        SavingUser.put("Skill",HandymanSkill);
        myRef.updateChildren(SavingUser);


        if(resultUri != null) {

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ProfilePhoto").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
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
                    newImage.put("profileImageUrl", downloadUrl);
                    myRef.updateChildren(newImage);

                    finish();
                    return;
                }
            });
        }else{
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImageView.setImageURI(resultUri);
        }
    }

}
