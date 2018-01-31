package com.uni.redcarpet.models;

/**
 * Created by delaroy on 4/13/17.
 */
public class User {
    private String uid;
    private String email;
    private String phoneNumber;
    private String firebaseToken;
    private String username;

    public User(){

    }

    public User(String uid, String email, String phoneNumber, String firebaseToken, String username){
        this.setUid(uid);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setFirebaseToken(firebaseToken);
        this.setUsername(username);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
