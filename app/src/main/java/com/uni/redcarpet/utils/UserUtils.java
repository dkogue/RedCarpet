package com.uni.redcarpet.utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uni.redcarpet.models.User;

/**
 * Created by cyrille on 26/01/2018.
 */

public class UserUtils {

    private DatabaseReference database;


    public void addUserToDatabase(final Context context, FirebaseUser firebaseUser) {
        database = FirebaseDatabase.getInstance().getReference();
        User user = new User(firebaseUser.getUid(),
                firebaseUser.getEmail(),firebaseUser.getPhoneNumber(),
                new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN), null);
        database.child(Constants.ARG_USERS)
                .child(firebaseUser.getUid())
                .setValue(user);
    }

    public void updateUserProfile(Context context, FirebaseUser firebaseUser, User user){
        database = FirebaseDatabase.getInstance().getReference();
        database.child(Constants.ARG_USERS).child(firebaseUser.getUid()).child("username").setValue(user.getUsername());
        database.child(Constants.ARG_USERS).child(firebaseUser.getUid()).child("email").setValue(user.getEmail());

    }
}
