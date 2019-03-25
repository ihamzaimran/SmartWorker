package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class requestSingleActivity extends AppCompatActivity {

    private TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestsingleactivity);

        name = (TextView)(findViewById(R.id.requestSingleName));
        Intent i = getIntent();
        String Name = i.getStringExtra("Name");
        name.setText(Name);
    }
}
