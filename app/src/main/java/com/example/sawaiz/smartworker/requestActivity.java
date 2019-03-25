package com.example.sawaiz.smartworker;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sawaiz.smartworker.requestRecyclerView.requestAdapter;
import com.example.sawaiz.smartworker.requestRecyclerView.requestObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class requestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView name,phone;
    private RecyclerView.Adapter mrequestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        recyclerView =(RecyclerView)(findViewById(R.id.requestRecyclerView));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        requestLayoutManager = new LinearLayoutManager(requestActivity.this);
        recyclerView.setLayoutManager(requestLayoutManager);
        mrequestAdapter = new requestAdapter(getDataSetRequest(),requestActivity.this);
        recyclerView.setAdapter(mrequestAdapter);


        getAllRequests();

    }

    private void getAllRequests() {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman");
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                      //  FetchRequestInformation(ds.getKey());
                        requestObject obj = ds.getValue(requestObject.class);
                        resultRequest.add(obj);
                    }
                    mrequestAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void FetchRequestInformation(String key) {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child("FirstName");
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.getKey();
                        requestObject obj = new requestObject(name);
                        resultRequest.add(obj);
                        mrequestAdapter.notifyDataSetChanged();
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private ArrayList resultRequest = new ArrayList<requestObject>();
    private List<requestObject> getDataSetRequest() {
        return resultRequest;
    }


}
