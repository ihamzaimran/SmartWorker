package com.example.sawaiz.smartworker.appointmentsRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.requestRecyclerView.requestObject;
import com.example.sawaiz.smartworker.requestRecyclerView.requestViewHolder;

import java.util.List;

public class appointmentAdapter extends RecyclerView.Adapter<appointmentViewHolder> {
    private List<appointmentObject> itemList;
    private Context context;

    public appointmentAdapter(List<appointmentObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public appointmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.futureappointment_item,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        appointmentViewHolder rvh = new appointmentViewHolder(view);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull appointmentViewHolder appointmentViewHolder, int i) {
        String name = itemList.get(i).getFirstName()+" "+itemList.get(i).getLastName();
        appointmentViewHolder.name.setText(i+1+". You've an appointment with "+name+" on "+itemList.get(i).getDate()+" at "+itemList.get(i).getTime());
        appointmentViewHolder.key.setText(itemList.get(i).getKey());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
