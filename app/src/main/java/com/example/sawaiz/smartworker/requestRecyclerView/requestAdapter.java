package com.example.sawaiz.smartworker.requestRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.requestActivity;

import java.util.List;

public class requestAdapter extends RecyclerView.Adapter<requestViewHolder> {
    private List<requestObject> itemList;
    private Context context;

    public requestAdapter(List<requestObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public requestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requestitem,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        requestViewHolder rvh = new requestViewHolder(view);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull requestViewHolder requestViewHolder, int i) {
        String Date = "Date: "+ itemList.get(i).getDate();
        String Time = "Time: " +itemList.get(i).getTime();
        String Key = itemList.get(i).getKey();


            requestViewHolder.date.setText(Date);
            requestViewHolder.time.setText(Time);
            requestViewHolder.key.setText(Key);


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
