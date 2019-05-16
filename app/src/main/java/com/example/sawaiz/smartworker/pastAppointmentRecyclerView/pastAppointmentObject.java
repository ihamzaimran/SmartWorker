package com.example.sawaiz.smartworker.pastAppointmentRecyclerView;

public class pastAppointmentObject {

    private String  Date, Time,Key;


    public pastAppointmentObject(String Date, String Time, String Key) {

        this.Date = Date;
        this.Time =Time;
        this.Key = Key;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
