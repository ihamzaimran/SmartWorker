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

    public TextView name;

    public requestViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        name = (TextView)itemView.findViewById(R.id.requestName);

    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), requestSingleActivity.class);
        intent.putExtra("Name", name.getText().toString());
        v.getContext().startActivity(intent);

    }
}
