package com.example.sawaiz.smartworker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;

import com.example.sawaiz.smartworker.MapsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {


    private Button mAvailability,mProjects,mNotifications,mProfile,mComplaints,mRequests;
    private boolean isLoggingOut = false;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        mAvailability = (Button)(findViewById(R.id.MainnMapBtn));
        mProjects = (Button)(findViewById(R.id.MainProjects));
        mNotifications = (Button)(findViewById(R.id.MainNotifications));
        mComplaints = (Button)(findViewById(R.id.MainComplaints));
        mProfile = (Button)(findViewById(R.id.MainProfile));
        mRequests = (Button)(findViewById(R.id.Mainrequests));



        mAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });


        mRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MainMenu.this, requestActivity.class);
                startActivity(i);
                return;
            }
        });


        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MainMenu.this, HandymanSettings.class);
                startActivity(i);
                return;
            }
        });


        mNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MainMenu.this, Notifications.class);
                startActivity(i);
                return;
            }
        });

        mProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MainMenu.this, ProjectsActivity.class);
                startActivity(i);
                return;
            }
        });


        mComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, ComplaintActivitty.class);
                startActivity(i);
                return;
            }
        });
    }
}
