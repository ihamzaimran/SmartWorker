package com.example.sawaiz.smartworker;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private TextView forgotPassword;
    private TextView createAccount;

    private EditText email;
    private EditText password;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();



        progressBar = (ProgressBar)(findViewById(R.id.LogprogressBar));

        email = (EditText)(findViewById(R.id.LoginuserName_txt));
        password = (EditText)(findViewById(R.id.LoginPassword_txt));

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
            finish();
        }


        //startService(new Intent(LoginActivity.this, onAppKilled.class));
        loginBtn = (Button)(findViewById(R.id.LoginBtn));
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString();
                 final String Password = password.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //authenticate user
                mAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    progressBar.setVisibility(View.GONE);
                                    if (password.length() < 6) {
                                        password.setError("Please enter correct password");
                                    } else {
                                        email.setText("");
                                        password.setText("");
                                        Toast.makeText(LoginActivity.this, "Authentication failed, check your email and password",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }
                        });
            } // end of onclick
        });


        createAccount = (TextView)(findViewById(R.id.loginCreateAccount_lbl));
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAccountIntent = new Intent(LoginActivity.this, Registration.class);
                startActivity(createAccountIntent);
                return;
            }
        });

        forgotPassword = (TextView) (findViewById(R.id.LoginforgorPassword_lbl));
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, fogotpassword.class);
                startActivity(forgotPasswordIntent);
                return;
            }
        });


    }


}
