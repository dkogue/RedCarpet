package com.uni.redcarpet.ui.activities;

public class WelcomeActivity {/*
    private static final int SPLASH_TIME_MS = 4000;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // Verify if the user is already logged in or not
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // if the user is logged in redirect the user to user listing activity
                    UserListingActivity.startActivity(WelcomeActivity.this);
                } else {
                    // otherwise redirect the user to login activity
                    LoginActivity.startIntent(WelcomeActivity.this);
                }
                finish();
            }
        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }
*/
}
