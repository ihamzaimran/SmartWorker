package com.example.sawaiz.smartworker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sawaiz.smartworker.appointmentsRecyclerView.appointmentAdapter;
import com.example.sawaiz.smartworker.appointmentsRecyclerView.appointmentObject;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class futureProjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAppointmentAdapter;
    private RecyclerView.LayoutManager appointmentLayoutManager;

    private String userId,customerId,date,time,Key,k;

    public futureProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_future_projects, container, false);

        recyclerView =(RecyclerView)view.findViewById(R.id.futureAppointmentRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        appointmentLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(appointmentLayoutManager);
        mAppointmentAdapter = new appointmentAdapter(getDataSetRequest(),getActivity());
        recyclerView.setAdapter(mAppointmentAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getAllRequests();

        return view;
    }

    private void getAllRequests() {
        DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("FutureAppointments");
        mydbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultRequest.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        k = ds.getKey();
                        DatabaseReference mydbref1 = FirebaseDatabase.getInstance().getReference().child("FutureAppointments").child(k);
                        mydbref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Key = dataSnapshot.getKey();

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (child.getKey().equals("Time")) {
                                            time = child.getValue().toString();
                                            //Log.e("time: ",time);
                                        }
                                        if (child.getKey().equals("Date")) {
                                            date = child.getValue().toString();
                                            //Log.e("date: ",date);
                                        }
                                        if (child.getKey().equals("CustomerId")) {
                                            customerId = child.getValue().toString();
                                        }
                                    }

                                    Log.e("Cid: ", customerId);
                                    appointmentObject obj = new appointmentObject(date,time,Key);
                                    resultRequest.add(obj);
                                    mAppointmentAdapter.notifyDataSetChanged();
                                    //progressDialog.dismiss();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
                //progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database Error: ",databaseError.getMessage());
            }
        });
        //progressDialog.dismiss();
    }



    private ArrayList resultRequest = new ArrayList<appointmentObject>();
    private List<appointmentObject> getDataSetRequest() {
        return resultRequest;
    }


}



