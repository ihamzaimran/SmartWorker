package com.example.sawaiz.smartworker.currentAppointmentRecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.currentSingleShow;

public class currentAppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView date,time,key,info;

    public currentAppointmentViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        date = (TextView)itemView.findViewById(R.id.currentDate);
        time = (TextView)itemView.findViewById(R.id.currentTime);
        key = (TextView)itemView.findViewById(R.id.currentKey);
        info = (TextView)itemView.findViewById(R.id.requestinfo);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), currentSingleShow.class);
        intent.putExtra("key", key.getText().toString());
        v.getContext().startActivity(intent);
    }
}
