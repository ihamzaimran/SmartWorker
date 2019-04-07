package com.example.sawaiz.smartworker.appointmentsRecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sawaiz.smartworker.R;
import com.example.sawaiz.smartworker.futureSingleProfileView;


public class appointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name,key;

    public appointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        name = (TextView)itemView.findViewById(R.id.appointmentName);
        key = (TextView)itemView.findViewById(R.id.appointmentinfo);

    }



    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), futureSingleProfileView.class);
        intent.putExtra("key", key.getText().toString());
        v.getContext().startActivity(intent);

    }
}
