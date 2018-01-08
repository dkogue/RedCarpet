package com.uni.redcarpet.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;
import com.uni.redcarpet.R;
import com.uni.redcarpet.ui.fragments.EventsFragment;

public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // show the list fragment as the main page
        fragment = new EventsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment).commit();


        //  We initialize the bottom bar here
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home);
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.navigation, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {

                switch (itemId) {
                    case R.id.navigation_events_id:
                        Snackbar.make(coordinatorLayout, "Event List", Snackbar.LENGTH_SHORT).show();
                        fragment = new EventsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.navigation_chat_id:
                        // fragment = new MapFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                        Snackbar.make(coordinatorLayout, "Chat", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_checkin_id:
                        // fragment = new CalendarFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                        Snackbar.make(coordinatorLayout, "Check In/Out", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_settings_id:
                        Bundle args = new Bundle();
                        //fragment = new MoreFragment();
                        //fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                        Snackbar.make(coordinatorLayout, "Settings", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

        });

        bottomBar.setActiveTabColor("#C2185B");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //no inspection Simplifiable If Statement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Use intent to call start the activity from outside the class

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

}
