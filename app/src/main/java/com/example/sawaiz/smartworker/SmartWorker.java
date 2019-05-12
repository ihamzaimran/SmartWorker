package com.example.sawaiz.smartworker;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SmartWorker extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
