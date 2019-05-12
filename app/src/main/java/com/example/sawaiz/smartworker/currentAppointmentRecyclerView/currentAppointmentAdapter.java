package com.example.sawaiz.smartworker.currentAppointmentRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sawaiz.smartworker.R;

import java.util.List;

public class currentAppointmentAdapter extends RecyclerView.Adapter<currentAppointmentViewHolder> {

    private List<currentAppointmentObject> itemList;
    private Context context;

    public currentAppointmentAdapter(List<currentAppointmentObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public currentAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_appointment_item,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        currentAppointmentViewHolder cavh = new currentAppointmentViewHolder(view);
        return cavh;
    }

    @Override
    public void onBindViewHolder(@NonNull currentAppointmentViewHolder currentAppointmentViewHolder, int i) {
        String Date = "Date: "+ itemList.get(i).getDate();
        String Time = "Time: " +itemList.get(i).getTime();
        String Key = itemList.get(i).getKey();


        currentAppointmentViewHolder.date.setText(Date);
        currentAppointmentViewHolder.time.setText(Time);
        currentAppointmentViewHolder.key.setText(Key);


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
