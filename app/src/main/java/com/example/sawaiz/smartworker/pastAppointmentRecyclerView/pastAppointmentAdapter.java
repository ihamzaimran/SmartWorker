package com.example.sawaiz.smartworker.pastAppointmentRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sawaiz.smartworker.R;

import java.util.List;

public class pastAppointmentAdapter extends RecyclerView.Adapter<pastAppointmentViewHolder> {

    private List<pastAppointmentObject> itemList;
    private Context context;

    public pastAppointmentAdapter(List<pastAppointmentObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public pastAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_item,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        pastAppointmentViewHolder pavh = new pastAppointmentViewHolder(view);
        return pavh;
    }

    @Override
    public void onBindViewHolder(@NonNull pastAppointmentViewHolder pastAppointmentViewHolder, int i) {
        String Date = "Date: "+ itemList.get(i).getDate();
        String Time = "Time: " +itemList.get(i).getTime();
        String Key = itemList.get(i).getKey();


        pastAppointmentViewHolder.date.setText(Date);
        pastAppointmentViewHolder.time.setText(Time);
        pastAppointmentViewHolder.key.setText(Key);


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
