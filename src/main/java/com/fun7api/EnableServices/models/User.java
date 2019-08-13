package com.fun7api.EnableServices.models;

import com.google.api.Advice;

public class User {
    //  Object properties
    private String userid;
    private Long numberOfApiCalls;

    //  Entity field names
    public static final String USER_ID = "userid";
    public static final String NUMBER_OF_API_CALLS = "apiCalls";

    // Constructors
    public User(String userid, Long numberOfApiCalls) {
        this.userid = userid;
        this.numberOfApiCalls = numberOfApiCalls;
    }

    private User(Builder builder) {
        this.userid = builder.userid;
        this.numberOfApiCalls = builder.numberofApiCalls;
    }

    public User() {

    }
    //  GETTERS
    public Long getNumberOfCalls() {
        return this.numberOfApiCalls;
    }

    public String getUserid() {
        return this.userid;
    }

    // SETTERS
    public void setNumberOfCalls(Long numberOfApiCalls) {
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

    // Builder class builds new User object with chaining methods.
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


