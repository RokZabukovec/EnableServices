package com.fun7api.EnableServices.models;

import com.google.api.Advice;

public class User {
    private String userid;
    private int numberOfApiCalls;


    public static final String USER_ID = "userid";
    public static final String NUMBER_OF_API_CALLS = "apiCalls";


    public User(String userid, int numberOfApiCalls) {
        this.userid = userid;
        this.numberOfApiCalls = numberOfApiCalls;

    }
    public User() {

    }

    public int getNumberOfCalls() {
        return this.numberOfApiCalls;
    }

    public String getUserid() {
        return this.userid;
    }
    public void setNumberOfCalls(int numberOfApiCalls) {
        this.numberOfApiCalls = numberOfApiCalls;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    @Override
    public String toString() {
        return
                "User ID: " + this.userid + " number of api calls: " + this.numberOfApiCalls;
    }

}


