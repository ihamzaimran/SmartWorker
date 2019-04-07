package com.example.sawaiz.smartworker;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sawaiz.smartworker.appointmentsRecyclerView.appointmentObject;
import com.example.sawaiz.smartworker.requestRecyclerView.requestAdapter;
import com.example.sawaiz.smartworker.requestRecyclerView.requestObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class requestActivity extends AppCompatActivity {


    private String userId,fname,lname,currentUserId, customerId,date,time,phone,k;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mrequestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

       //progressDialog = new ProgressDialog(requestActivity.this);
        //progressDialog.setMessage("Loading Requests...");
        //progressDialog.setCancelable(false);
        //progressDialog.show();

        recyclerView =(RecyclerView)(findViewById(R.id.requestRecyclerView));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        requestLayoutManager = new LinearLayoutManager(requestActivity.this);
        recyclerView.setLayoutManager(requestLayoutManager);
        mrequestAdapter = new requestAdapter(getDataSetRequest(),requestActivity.this);
        recyclerView.setAdapter(mrequestAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getAllRequests();

    }

    private void getAllRequests() {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("RequestList");
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultRequest.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot request : dataSnapshot.getChildren()){
                        FetchRequestInformation(request.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void FetchRequestInformation(String CustomerKey) {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("AppointmentRequests").child(CustomerKey);
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String requestID = dataSnapshot.getKey();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Time")) {
                            time = child.getValue().toString();
                        }
                        if (child.getKey().equals("Date")) {
                            date = child.getValue().toString();
                        }
                        if (child.getKey().equals("CustomerId")) {
                            customerId = child.getValue().toString();
                        }

                    }
                    requestObject obj = new requestObject(date,time,requestID);
                    resultRequest.add(obj);
                    mrequestAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //progressDialog.dismiss();
    }

    /*

    private void getUserInformation(String Customer, String otherUserId, final String key, String Date,  String Time ) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(otherUserId);
        mOtherUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("FirstName") != null){
                        fname = map.get("FirstName").toString();
                    }
                    if(map.get("PhoneNumber") != null){
                        phone = map.get("PhoneNumber").toString();
                    }
                    if(map.get("LastName") != null){
                        lname = map.get("LastName").toString();
                    }
                    Log.e("date: ",date);
                    Log.e("time: ",time);
                    requestObject obj = new requestObject(fname,lname,phone,date,time,key);
                    resultRequest.add(obj);
                    mrequestAdapter.notifyDataSetChanged();

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database Error: ",databaseError.getMessage());
            }
        });
    }


*/


    private ArrayList resultRequest = new ArrayList<requestObject>();
    private List<requestObject> getDataSetRequest() {
        return resultRequest;
    }


}
