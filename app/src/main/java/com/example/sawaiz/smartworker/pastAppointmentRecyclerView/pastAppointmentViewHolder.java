package com.example.sawaiz.smartworker.pastAppointmentRecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.pastSingleShow;

public class pastAppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView date,time,key;

    public pastAppointmentViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        date = (TextView)itemView.findViewById(R.id.pastDate);
        time = (TextView)itemView.findViewById(R.id.pastTime);
        key = (TextView)itemView.findViewById(R.id.pastKey);
        //info = (TextView)itemView.findViewById(R.id.requestinfo);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), pastSingleShow.class);
        intent.putExtra("key", key.getText().toString());
        v.getContext().startActivity(intent);
    }
}
