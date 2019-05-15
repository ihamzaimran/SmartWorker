package com.example.sawaiz.smartworker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class TaskReminderReceiver extends BroadcastReceiver {
    private static final String ID = "snakexmon";
    private DatabaseReference databaseReference,hdb,cdb,db;;
    private int reqcode;
    private String msg;
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c = Calendar.getInstance();
        reqcode = (int) c.getTimeInMillis();

        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String cid = intent.getStringExtra("cid");
        String hid = intent.getStringExtra("hid");
        String Key = intent.getStringExtra("key");
        msg = "You've an appointment today on "+date;

        Intent notificationIntent = new Intent(context, notify.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(notify.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(reqcode, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Reminder!")
                .setTicker("New Message Alert!")
                .setStyle(new Notification.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setSmallIcon(R.drawable.applog)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000}).build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID,
                    "Reminder",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(reqcode, notification);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference handyman = FirebaseDatabase.getInstance().getReference().
                child("Users").child("Handyman").child(userId).child("CurrentAppointments");
        handyman.keepSynced(true);
        DatabaseReference customerDB = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Customer").child(cid).child("CurrentAppointments");
        customerDB.keepSynced(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CurrentAppointments");

        //String CurrentAppointmentsId = databaseReference.push().getKey();
        handyman.child(Key).setValue(true);
        customerDB.child(Key).setValue(true);

        Map data = new HashMap();
        data.put("HandymanId",userId);
        data.put("Date",date);
        data.put("Time",time);
        data.put("CustomerId",cid);
        data.put("HandymanStart","NotStarted");
        data.put("CustomerStart","NotStarted");
        databaseReference.child(Key).updateChildren(data);

        db =  FirebaseDatabase.getInstance().getReference().child("FutureAppointments").child(Key);
        hdb = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("FutureAppointments").child(Key);
        cdb =  FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(cid).child("FutureAppointments").child(Key);

        db.keepSynced(true);
        hdb.keepSynced(true);
        cdb.keepSynced(true);

        hdb.removeValue();
        cdb.removeValue();
        db.removeValue();

    }
}
