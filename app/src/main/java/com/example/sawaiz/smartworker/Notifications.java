package com.example.sawaiz.smartworker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.sawaiz.smartworker.notificationRecyclerView.notificationAdapter;
import com.example.sawaiz.smartworker.notificationRecyclerView.notificationObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {
    private String userId,message;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mnotificationAdapter;
    private RecyclerView.LayoutManager notificationLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView =(RecyclerView)(findViewById(R.id.notificationRecyclerView));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        notificationLayoutManager = new LinearLayoutManager(Notifications.this);
        recyclerView.setLayoutManager(notificationLayoutManager);
        mnotificationAdapter = new notificationAdapter(getDataSetRequest(),Notifications.this);
        recyclerView.setAdapter(mnotificationAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getAllNotifications();
    }

    private void getAllNotifications() {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("Notification");
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // resultNotification.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        getNotifications(key);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNotifications(String key) {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Handyman").child(userId).child("Notification").child(key);

        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String requestID = dataSnapshot.getKey();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("Message")) {
                            message = child.getValue().toString();
                        }
                    }
                    notificationObject obj = new notificationObject(message);
                    resultNotification.add(obj);
                    mnotificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private ArrayList resultNotification = new ArrayList<notificationObject>();
    private List<notificationObject> getDataSetRequest() {
        return resultNotification;
    }

}
