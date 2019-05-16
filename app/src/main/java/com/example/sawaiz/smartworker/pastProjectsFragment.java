package com.example.sawaiz.smartworker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sawaiz.smartworker.pastAppointmentRecyclerView.pastAppointmentAdapter;
import com.example.sawaiz.smartworker.pastAppointmentRecyclerView.pastAppointmentObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class pastProjectsFragment extends Fragment {

    private String userId,fname,lname,currentUserId, customerId,date,time,phone,k;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mrequestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;

    public pastProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_projects, container, false);

        recyclerView =(RecyclerView)(RecyclerView)view.findViewById(R.id.pastAppointmentRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        requestLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(requestLayoutManager);
        mrequestAdapter = new pastAppointmentAdapter(getDataSetRequest(),getActivity());
        recyclerView.setAdapter(mrequestAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getAllRequests();


        return view;
    }

    private void getAllRequests() {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Handyman").child(userId).child("PastAppointments");
        mydbref.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference()
                .child("PastAppointments").child(CustomerKey);
        mydbref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    pastAppointmentObject obj = new pastAppointmentObject(date,time,requestID);
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


    private ArrayList resultRequest = new ArrayList<pastAppointmentObject>();
    private List<pastAppointmentObject> getDataSetRequest() {
        return resultRequest;
    }

}
