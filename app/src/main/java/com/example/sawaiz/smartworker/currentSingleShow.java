package com.example.sawaiz.smartworker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sawaiz.smartworker.Utils.SendNotification;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class currentSingleShow extends AppCompatActivity {

    private String Key,userId,customerId,handymanId,customerName,Skill,checkCustomerState,checkHandymanState,notiKey;
    private Button start,stop;
    private TextView name,skill;
    private ImageView profile;
    private DatabaseReference mydbref,startJobRef,checkJob,stopJobRef,reasonRef;
    private ProgressDialog progressDialog;
    private LinearLayout timerLayout;
    private Chronometer timer;
    private boolean running;
    private long pause;
    View view;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_single_show);

        Intent i = getIntent();
        Key = i.getStringExtra("key");


        start = (Button)(findViewById(R.id.currentStartJob));
        stop = (Button)(findViewById(R.id.currentforcedStop));

        name = (TextView)(findViewById(R.id.currentUserName_txt));
        skill = (TextView)(findViewById(R.id.currentSkill_txt));

        profile = (ImageView)(findViewById(R.id.currentProfile));

        timerLayout = (LinearLayout)(findViewById(R.id.CurrentTimerLayout));

        timer = (Chronometer)(findViewById(R.id.jobTimer));
        timer.setText("00:00:00");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mydbref = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
        getInfo();



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJobRef = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
                startJobRef.child("HandymanStart").setValue("start");
                checkJobStarted();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopJobRef = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
                stopJobRef.child("HandymanStart").setValue("ForceStopped");
            }
        });
    }

    private void checkJobStarted() {
        checkJob = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
        checkJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().equals("CustomerStart")){
                            checkCustomerState = ds.getValue().toString();
                        }
                        if(ds.getKey().equals("HandymanStart")) {
                            checkHandymanState = ds.getValue().toString();
                            if(checkHandymanState.equals("start")&&checkCustomerState.equals("start")){
                                timerLayout.setVisibility(View.VISIBLE);
                                stop.setVisibility(View.VISIBLE);
                                startTimer(view);
                                start.setVisibility(View.INVISIBLE);
                            }
                            else{
                                //start.setVisibility(View.VISIBLE);
                                stopTimer(view);
                                stop.setVisibility(View.INVISIBLE);
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startTimer(View v) {
        if(!running){
            timer.setBase(SystemClock.elapsedRealtime() - pause);
            timer.start();
            running = true;
        }
    }

    private void stopTimer(View v) {
        if (running) {
            timer.stop();
            pause = SystemClock.elapsedRealtime() - timer.getBase();
            running = false;
            calculateTime(pause);
            showReasonDialog();
        }
    }

    private void showReasonDialog() {
        reasonRef = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.cancel_appointment_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Reason");
        alertDialogBuilder.setMessage("Provide reason for forced stopping the job?");
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.reason_txt);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                reasonRef.child("ForceStoppedJobReason").setValue(input);
                                new SendNotification(input,"Forced Stopped Job Reason",notiKey);
                                Intent data = new Intent(currentSingleShow.this,TransctionActivity.class);
                                data.putExtra("key",Key);
                                startActivity(data);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void calculateTime(long pause) {
        int hours = (int) (pause / 3600000);
        int minutes = (int) (pause - hours * 3600000) / 60000;
        int seconds = (int) (pause - hours * 3600000 - minutes * 60000) / 1000;
        Log.e("hours",String.valueOf(hours));
        Log.e("minutes",String.valueOf(minutes));
        Log.e("second",String.valueOf(seconds));

    }

    private void getInfo() {
        mydbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot child:dataSnapshot.getChildren()){
                        if (child.getKey().equals("CustomerId")){
                            customerId = child.getValue().toString();
                            getUserInformation("Customer", customerId);
                        }
                        if (child.getKey().equals("HandymanId")){
                            handymanId = child.getValue().toString();
                            gethandyInformation("Handyman", handymanId);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void gethandyInformation(String handyman, String handymanId) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(handyman).child(handymanId);
        mOtherUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Skill") != null){
                        Skill = map.get("Skill").toString();
                        skill.setText(Skill);
                    }

                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getUserInformation(String customer, String customerId) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(customer).child(customerId);
        mOtherUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("FirstName") != null){
                        customerName = map.get("FirstName").toString();
                    }
                    if(map.get("LastName") != null){
                        customerName = customerName + " "+map.get("LastName").toString();
                        name.setText(customerName);
                    }
                    if(map.get("notificationKey") != null){
                        notiKey = map.get("notificationKey").toString();
                    }

                    if(map.get("profileImageUrl") != null){
                        String url = map.get("profileImageUrl").toString();
                        Picasso.get().load(url).fit().into(profile);
                    }
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

}
