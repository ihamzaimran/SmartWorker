package com.example.sawaiz.smartworker.notificationRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sawaiz.smartworker.R;

public class notificationViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView message;

    public notificationViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        message = (TextView)itemView.findViewById(R.id.notificationinfo);

    }

    @Override
    public void onClick(View v) {

    }
}
