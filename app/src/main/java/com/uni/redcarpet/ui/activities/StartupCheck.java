package com.uni.redcarpet.ui.activities;

import android.app.Application;

public class StartupCheck extends Application {

    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        StartupCheck.sIsChatActivityOpen = isChatActivityOpen;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // setContentView(R.layout.activity_main);
    }
}
