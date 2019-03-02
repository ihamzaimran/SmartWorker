package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private Button createAccount;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText mEmail,mPassword,mPhone,mCNIC,mFname,mLname;
    private EditText costTxt;
    private RadioGroup radioGroup;
     String experience;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar)(findViewById(R.id.RegprogressBar));

        mCNIC = (EditText)(findViewById(R.id.rCnic_txt));
        mEmail = (EditText)(findViewById(R.id.rEmail_txt));
        mFname = (EditText)(findViewById(R.id.rFname_txt));
        mLname = (EditText)(findViewById(R.id.rLname_txt));
        mPassword = (EditText)(findViewById(R.id.rPassword_txt));
        mPhone = (EditText)(findViewById(R.id.rPhoneNo_txt));


        costTxt = (EditText) (findViewById(R.id.ECcost_txt));



        createAccount = (Button)(findViewById(R.id.rCreateAccountBtn));
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String cnic = mCNIC.getText().toString();
                final String fname = mFname.getText().toString();
                final String lname = mLname.getText().toString();
                final String phone = mPhone.getText().toString();


                //createAccount.setEnabled(false);

                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(getApplicationContext(), "Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lname)) {
                    Toast.makeText(getApplicationContext(), "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password too short, enter minimum 6 characters!");
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(cnic)) {
                    Toast.makeText(getApplicationContext(), "Enter CNIC!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cnic.length() > 13 || cnic.length() <13 ) {
                   mCNIC.setError("Invalid CNIC!");
                    Toast.makeText(getApplicationContext(), "Invalid CNIC! Please enter a valid CNIC for Verification!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Enter CONTACT NO!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.length() > 10 || phone.length() <10 ) {
                    mPhone.setError("Invalid Phone Number!");
                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Registration.this, "Registration failed."
                                    + task.getException(), Toast.LENGTH_SHORT).show();
                            createAccount.setEnabled(true);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            String user_id = mAuth.getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            myRef = database.getReference().child("Users").child("Handyman").child(user_id);
                            myRef.setValue("true");
                            saveHandyman();
                            startActivity(new Intent(Registration.this, CNIC.class));
                            finish();
                        }
                    }

                });

            }



        });

    }

    public void saveHandyman()
    {
        Map SavingUser = new HashMap();
        SavingUser.put("FirstName",mFname.getText().toString());
        SavingUser.put("LastName",mLname.getText().toString());
        SavingUser.put("EmailAddress",mEmail.getText().toString());
        SavingUser.put("PhoneNumber",mPhone.getText().toString());
        SavingUser.put("CNIC",mCNIC.getText().toString());
        SavingUser.put("PhoneNumber","+92"+mPhone.getText().toString());
        SavingUser.put("Cost per Hour",costTxt.getText().toString());
        SavingUser.put("Experience",experience);
        myRef.updateChildren(SavingUser);

    }



}
