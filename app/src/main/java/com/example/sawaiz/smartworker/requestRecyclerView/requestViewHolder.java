package com.example.sawaiz.smartworker.requestRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.requestSingleActivity;

public class requestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView datentime,key;

    public requestViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        datentime = (TextView)itemView.findViewById(R.id.requestinfo);
        key = (TextView)itemView.findViewById(R.id.requestKey);

    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), requestSingleActivity.class);
        intent.putExtra("key", key.getText().toString());
        v.getContext().startActivity(intent);

    }
}