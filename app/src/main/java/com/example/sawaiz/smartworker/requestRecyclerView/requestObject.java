package com.example.sawaiz.smartworker.requestRecyclerView;

public class requestObject {

    private String FirstName, LastName;

    public requestObject(){

    }

    public requestObject(String FirstName) {
        this.FirstName = FirstName;
    }

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
}
