package com.example.sawaiz.smartworker.notificationRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.sawaiz.smartworker.R;

import java.util.List;

public class notificationAdapter extends RecyclerView.Adapter<notificationViewHolder> {

    private List<notificationObject> itemList;
    private Context context;

    public notificationAdapter(List<notificationObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;

    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        notificationViewHolder nvh = new notificationViewHolder(view);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder notificationViewHolder, int i) {
        String message = itemList.get(i).getMessage();

        notificationViewHolder.message.setText(message);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
