package com.uni.redcarpet.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIdServicePhoneAuth extends FirebaseInstanceIdService {

    private static final String TAG = "NOTIFICATION";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);
    }


}
