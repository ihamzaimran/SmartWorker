/*
package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private LinearLayout PhoneLayout;
    private LinearLayout CodeLayout;


    private Button sendBtn;
    private boolean btnType = false;

    private FirebaseAuth mAuth;
    private TextView VisibleErrorMessage;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String PhoneNumber;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);

        TextView linkbacktologin = (TextView)(findViewById(R.id.loginScreen));

        VisibleErrorMessage = (TextView)(findViewById(R.id.ErrorMessage_lbl));


        final EditText PhoneInput = (EditText) (findViewById(R.id.phoneNumber_txt));
        final EditText CodeInput = (EditText) (findViewById(R.id.code_txt));

        CodeLayout = (LinearLayout) (findViewById(R.id.codeLayout));


         final Button sendBtn = (Button)(findViewById(R.id.SendBtn));

         mAuth = FirebaseAuth.getInstance();

         sendBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(PhoneInput.getText().toString() == "" || PhoneInput.getText().toString().length() != 10 ){

                     Toast.makeText(getApplicationContext(), "Please Enter correct Phone Number", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     if(btnType == false){

                         PhoneInput.setEnabled(false);
                         sendBtn.setEnabled(false);

                         PhoneNumber = "+92"+PhoneInput.getText().toString();
                         PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneNumber, 60, TimeUnit.SECONDS,
                                 PhoneAuthActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                             @Override
                             public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                             {
                                 signInWithPhoneAuthCredential(phoneAuthCredential);
                             }

                             @Override
                             public void onVerificationFailed(FirebaseException e)
                             {
                                 VisibleErrorMessage.setVisibility(View.VISIBLE);

                             }
                             @Override
                             public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                                 // Save verification ID and resending token so we can use them later
                                 mVerificationId = verificationId;
                                 mResendToken = token;

                                 btnType = true;


                                 CodeLayout.setVisibility(View.VISIBLE);

                                 sendBtn.setText("Verify Code");
                                 sendBtn.setEnabled(true);

                                 // ...
                             }
                         });
                     }
                     else
                     {
                         sendBtn.setEnabled(false);
                         String VerificationCode = CodeInput.getText().toString();
                         PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, VerificationCode);
                         signInWithPhoneAuthCredential(credential);


                     }
                 }
             }

         });



        linkbacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();
                            Intent EmailIntent = new Intent(PhoneAuthActivity.this,Registration.class);
                            EmailIntent.putExtra("Phone",PhoneNumber);
                            startActivity(EmailIntent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            VisibleErrorMessage.setText("Invalid");
                            VisibleErrorMessage.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Invalid Code. Try Again,", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }


                });
    }




}
*/