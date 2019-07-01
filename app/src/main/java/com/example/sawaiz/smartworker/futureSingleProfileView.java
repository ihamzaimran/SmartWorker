package com.example.sawaiz.smartworker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sawaiz.smartworker.Utils.SendNotification;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class futureSingleProfileView extends AppCompatActivity implements OnMapReadyCallback{

    String userId,customerId,handymanId,name,latlng, lat,lng,date,time,Key,notiKey;

    private TextView userName;
    private TextView userPhone;

    private TextView userdate;
    private TextView usertime;

    private Button accept;
    private Button reject;

    private ImageView userImage;

    private LatLng locationLatLng;

    private DatabaseReference mydbref,hdb,cdb,db;

    private ProgressDialog progressDialog;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    Location location;
    Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_single_profile_view);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(futureSingleProfileView.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }else{
            mMapFragment.getMapAsync(this);
        }


        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        userPhone = (TextView) findViewById(R.id.userPhone);
        userdate = (TextView) findViewById(R.id.JobDate);
        usertime = (TextView) findViewById(R.id.JobTime);


        Intent i = getIntent();
        Key = i.getStringExtra("key");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mydbref = FirebaseDatabase.getInstance().getReference().child("FutureAppointments").child(Key);
        mydbref.keepSynced(true);

        getUserRequestInfo();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        reject = (Button)(findViewById(R.id.ReqReject));


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               displayCancelDialog();
            }
        });
    }

    private void displayCancelDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.reason_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Reason");
        alertDialogBuilder.setMessage("Provide reason for cancelling the request?");
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.reason_txt);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                if(input == ""){
                                    userInput.setError("Please provide a reason");
                                }
                                else {
                                    new SendNotification(input, "Request Cancellation Reason", notiKey);
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference handyman = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("CancelRequestsList");
                                    DatabaseReference customerDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(customerId).child("CancelRequestsList");
                                    DatabaseReference myDBref1 = FirebaseDatabase.getInstance().getReference().child("CancelRequests");
                                    String cancelRequestsID = myDBref1.push().getKey();
                                    handyman.child(cancelRequestsID).setValue(true);
                                    customerDB.child(cancelRequestsID).setValue(true);

                                    Map data = new HashMap();
                                    data.put("HandymanId", userId);
                                    data.put("CustomerId", customerId);
                                    data.put("Date", date);
                                    data.put("Time", time);


                                    myDBref1.child(cancelRequestsID).updateChildren(data);

                                    db = FirebaseDatabase.getInstance().getReference().child("FutureAppointments").child(Key);
                                    hdb = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("FutureAppointments").child(Key);
                                    cdb = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(customerId).child("FutureAppointments").child(Key);

                                    hdb.removeValue();
                                    cdb.removeValue();
                                    db.removeValue();
                                    finish();
                                }


                            }
                        })
                .setNeutralButton("Skip",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SendNotification("Reason not provided by handyman!", "Request Cancellation Reason", notiKey);
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference handyman = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("CancelRequestsList");
                                DatabaseReference customerDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(customerId).child("CancelRequestsList");
                                DatabaseReference myDBref1 = FirebaseDatabase.getInstance().getReference().child("CancelRequests");
                                String cancelRequestsID = myDBref1.push().getKey();
                                handyman.child(cancelRequestsID).setValue(true);
                                customerDB.child(cancelRequestsID).setValue(true);

                                Map data = new HashMap();
                                data.put("HandymanId", userId);
                                data.put("CustomerId", customerId);
                                data.put("Date", date);
                                data.put("Time", time);


                                myDBref1.child(cancelRequestsID).updateChildren(data);

                                db = FirebaseDatabase.getInstance().getReference().child("FutureAppointments").child(Key);
                                hdb = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("FutureAppointments").child(Key);
                                cdb = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(customerId).child("FutureAppointments").child(Key);

                                hdb.removeValue();
                                cdb.removeValue();
                                db.removeValue();
                                finish();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getUserRequestInfo() {
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
                        }

                        if (child.getKey().equals("Date")){
                            date = child.getValue().toString();
                            userdate.setText((child.getValue().toString()));
                        }
                        if (child.getKey().equals("Time")){
                            time = child.getValue().toString();
                            usertime.setText(child.getValue().toString());
                        }

                        if (child.getKey().equals("CustomerLocation")){
                            lat = child.child("lat").getValue().toString();
                            lng = child.child("lng").getValue().toString();
                            locationLatLng = new LatLng(Double.valueOf(child.child("lat").getValue().toString()),Double.valueOf(child.child("lng").getValue().toString()));
                            if(locationLatLng != new LatLng(0,0)) {
                                MarkeratCustomerLocation();
                            }
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


    private void MarkeratCustomerLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        mMap.addMarker(new MarkerOptions().position(locationLatLng).title("Job Location"));
    }


    private void getUserInformation(String customer, String otherUserId) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(customer).child(otherUserId);
        mOtherUserDB.keepSynced(true);
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
                        userName.setText(name);
                    }
                    if(map.get("PhoneNumber") != null){
                        userPhone.setText(map.get("PhoneNumber").toString());
                    }
                    if(map.get("notificationKey") != null){
                        notiKey = map.get("notificationKey").toString();
                    }
                    if(map.get("profileImageUrl") != null){
                        String url = map.get("profileImageUrl").toString();
                        Picasso.get().load(url).fit().into(userImage);
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




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(futureSingleProfileView.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        mMap.setMyLocationEnabled(true);
    }

    final int LOCATION_REQUEST_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mMapFragment.getMapAsync(this);
                } else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
