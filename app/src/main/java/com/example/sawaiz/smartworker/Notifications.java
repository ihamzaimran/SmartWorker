package com.example.sawaiz.smartworker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sawaiz.smartworker.Utils.SendNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Notifications extends AppCompatActivity {
private  long timeInMs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Button btn = (Button)(findViewById(R.id.checkbtn));
        Button set = (Button)(findViewById(R.id.alarmBtn));


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* EditText edit = (EditText)(findViewById(R.id.setID));
                 int i = Integer.parseInt(edit.getText().toString());*/
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(Notifications.this, TaskReminderReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                int hour = 1;
                int minute = 50;
                String myTime = String.valueOf(hour) + ":" + String.valueOf(minute);

                Date date = null;

                // today at your defined time Calendar
                Calendar cal = new GregorianCalendar();
                // set hours and minutes
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                Date customDate = cal.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {

                    date = sdf.parse(myTime);

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                if (date != null) {
                    timeInMs = customDate.getTime();
                }

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMs,pendingIntent);

                Toast.makeText(Notifications.this, "Alarm set",Toast.LENGTH_LONG).show();
            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification("You clicked on notification","Notification",null);
            }
        });
    }
}
