package com.example.sawaiz.smartworker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class pastSingleShow extends AppCompatActivity {

    private TextView customername, appoid, handyswork, handyscost, jobduration, totalamount,date,time,cname;
    private AppCompatRatingBar ratingBar;
    private DatabaseReference customerRating,myRef,customerReview,submitReviewRef,reasonRef,hdb,cdb,db;
    Context context = this;
    private String Key,customerId,handymanId,name;
    private float rate;
    private AppCompatRatingBar customerRatingBar;
    private Button report;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_single_show);


        Intent i = getIntent();
        Key = i.getStringExtra("key");

        Log.e("Key",Key);


        customername =(TextView)(findViewById(R.id.completecustomername));
        appoid =(TextView)(findViewById(R.id.completeappointmentid));
        handyswork =(TextView)(findViewById(R.id.completeskill));
        handyscost =(TextView)(findViewById(R.id.completecostperhour));
        jobduration =(TextView)(findViewById(R.id.completejobduration));
        totalamount =(TextView)(findViewById(R.id.completebill));
        date =(TextView)(findViewById(R.id.completedate)); 
        time =(TextView)(findViewById(R.id.completetime));
        cname =(TextView)(findViewById(R.id.cname));

        report =(Button)(findViewById(R.id.report));
        customerRatingBar = (AppCompatRatingBar)(findViewById(R.id.customerRatingBar));

        myRef = FirebaseDatabase.getInstance().getReference().child("PastAppointments").child(Key);
        getInfo();
        appoid.setText(Key);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        progressDialog.show();

    }


    private void getInfo() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        if (child.getKey().equals("CustomerRating")){
                            rate = Integer.valueOf(child.getValue().toString());
                             customerRatingBar.setRating(rate);
                        }
                        if (child.getKey().equals("JobDuration")){
                            jobduration.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("TotalBill")){
                            totalamount.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("Date")){
                            date.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("Time")){
                            time.setText(child.getValue().toString());
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
                        handyswork.setText(map.get("Skill").toString());
                    }
                    if(map.get("CostPerHour") != null){
                        handyscost.setText(map.get("Skill").toString());
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

    private void getUserInformation(String customer, String otherUserId) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(customer).child(otherUserId);
        mOtherUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("FirstName") != null){
                        name = map.get("FirstName").toString();
                    }
                    if(map.get("LastName") != null){
                        name = name + " "+map.get("LastName").toString();
                        customername.setText(name);
                        cname.setText(name+ "  ");
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
