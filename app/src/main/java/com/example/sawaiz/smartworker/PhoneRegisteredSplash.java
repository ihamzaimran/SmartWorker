package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PhoneRegisteredSplash extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_registered_splash);

        mAuth = FirebaseAuth.getInstance();

        Intent SplashIntent = getIntent();
        final String x = SplashIntent.getStringExtra("Phone");



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.signOut();


                Intent HomeIntent = new Intent(PhoneRegisteredSplash.this, Registration.class) ;
                HomeIntent.putExtra("Phone1",x);
                startActivity(HomeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            Intent i = new Intent(PhoneRegisteredSplash.this,PhoneAuthActivity.class);
            startActivity(i);
            finish();
        }


    }

}
