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

import java.util.Calendar;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class TaskReminderReceiver extends BroadcastReceiver {
    private static final String ID = "snakexmon";
    private int reqcode;
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c = Calendar.getInstance();
        reqcode = (int) c.getTimeInMillis();

        Intent notificationIntent = new Intent(context, notify.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(notify.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(reqcode, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Reminder!")
                .setContentText("You've an appointment today")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.applog)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000}).build();


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
    }
}
