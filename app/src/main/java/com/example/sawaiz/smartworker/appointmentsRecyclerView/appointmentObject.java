package com.example.sawaiz.smartworker.appointmentsRecyclerView;

public class appointmentObject {

    private String FirstName, LastName, PhoneNumber, Date, Time,Key;


    public appointmentObject(String Date, String Time, String Key) {

        this.Date = Date;
        this.Time =Time;
        this.Key = Key;
    }
/*
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

*/

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
