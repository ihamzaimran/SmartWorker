package com.example.sawaiz.smartworker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.sawaiz.smartworker.Utils.SendNotification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransctionActivity extends AppCompatActivity {

    private AppCompatRatingBar ratingBar;
    private DatabaseReference submitRatingRef,customerRating,myRef;
    Context context = this;
    private String Key,customerId,handymanId;
    private float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transction);

        Intent i = getIntent();
        Key = i.getStringExtra("key");

        myRef = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
        getInfo();
        displayRatingBarDialog();

        /*
for (DataSnapshot child : dataSnapshot.child("CustomerRating").getChildren()){
                        ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                        ratingsTotal++;
                    }
                    if(ratingsTotal!= 0){
                        ratingsAvg = ratingSum/ratingsTotal;
                        mRatingBar.setRating(ratingsAvg);
                    }

*/

    }

    private void getInfo() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot child:dataSnapshot.getChildren()){
                        if (child.getKey().equals("CustomerId")){
                            customerId = child.getValue().toString();
                            //getUserInformation("Customer", customerId);
                        }
                        if (child.getKey().equals("HandymanId")){
                            handymanId = child.getValue().toString();
                            //gethandyInformation("Handyman", handymanId);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayRatingBarDialog() {
        submitRatingRef = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments").child(Key);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.rating_bar_dialog, null);
        ratingBar = (AppCompatRatingBar)promptsView.findViewById(R.id.customerRatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Rate Handyman");
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                submitRatingRef.child("CustomerRating").setValue(rate);
                                customerRating = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child("Customer").child(customerId).child("CustomerRating");
                                customerRating.child("Key").setValue(rate);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
