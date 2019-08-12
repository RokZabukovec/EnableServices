package com.fun7api.EnableServices.models;

import com.google.api.Advice;

public class User {
    private String userid;
    private Long numberOfApiCalls;
    private String encodedID;


    public static final String USER_ID = "userid";
    public static final String NUMBER_OF_API_CALLS = "apiCalls";


    public User(String userid, Long numberOfApiCalls) {
        this.userid = userid;
        this.numberOfApiCalls = numberOfApiCalls;

    }
    public User(String userid, Long numberOfApiCalls, String encodedID) {
        this.userid = userid;
        this.numberOfApiCalls = numberOfApiCalls;
        this.encodedID = encodedID;

    }
    public User() {

    }

    private User(Builder builder) {
        this.userid = builder.userid;
        this.numberOfApiCalls = builder.numberofApiCalls;

    }

    public Long getNumberOfCalls() {
        return this.numberOfApiCalls;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setNumberOfCalls(Long numberOfApiCalls) {
        this.numberOfApiCalls = numberOfApiCalls;
    }
    public void setEncodedID(String encodedID) {
        this.encodedID = encodedID;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }


    @Override
    public String toString() {
        return
                "User ID: " + this.userid + " number of api calls: " + this.numberOfApiCalls;
    }

    public static class Builder{
        private String userid;
        private Long numberofApiCalls;


        public Builder userId(String userid) {
            this.userid = userid;
            return this;
        }

        public Builder numberOfApiCalls(Long numberofApiCalls) {
            this.numberofApiCalls = numberofApiCalls;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}


