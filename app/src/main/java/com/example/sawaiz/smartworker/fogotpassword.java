package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class fogotpassword extends AppCompatActivity {

    private Button ResetBtn;
    private EditText forgot_txt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView forgotPasswordBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_activity);

        progressBar = (ProgressBar)(findViewById(R.id.forgotPasswordprogressBar));
        forgot_txt = (EditText)(findViewById(R.id.forgotPass_txt));
        mAuth = FirebaseAuth.getInstance();
        ResetBtn = (Button)(findViewById(R.id.resetBtn));
        forgotPasswordBack = (findViewById(R.id.forgotPassBack));

        forgotPasswordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgot_txt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Please enter email address.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(fogotpassword.this, "Instructions to reset password has been sent to your email.", Toast.LENGTH_SHORT).show();
                            Intent backToLogin = new Intent(fogotpassword.this, LoginActivity.class);
                            startActivity(backToLogin);
                            finish();
                            return;
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(fogotpassword.this, "Email not found!",
                                    Toast.LENGTH_SHORT).show();
                            ResetBtn.setEnabled(true);
                        }

                    }
                });
            }
        });




        }
}
