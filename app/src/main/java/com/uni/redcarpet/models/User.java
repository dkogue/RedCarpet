package com.uni.redcarpet.models;

/**
 * Created by delaroy on 4/13/17.
 */
public class User {
    public String uid;
    public String email;
    public String phoneNumber;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String email,String phoneNumber, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firebaseToken = firebaseToken;
    }
}
