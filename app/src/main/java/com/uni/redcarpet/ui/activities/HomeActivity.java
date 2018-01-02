package com.uni.redcarpet.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;
import com.uni.redcarpet.R;

public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home);
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.navigation, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemid) {

                //close the drawer when clicking the bottom bar
                //drawerLayout.closeDrawers();

                /*switch (itemId) {
                    case R.id.list_item:
                        Snackbar.make(coordinatorLayout, "List", Snackbar.LENGTH_SHORT).show();
                        //f = new ListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        break;
                    case R.id.map_item:
                       // f = new MapFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Map", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.calendar_item:
                       // f = new CalendarFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Calendar", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.more_item:
                        Bundle args = new Bundle();
                        args.putBoolean("hasLoggedIn", hasLoggedIn);
                        //f = new MoreFragment();
                        //f.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Settings", Snackbar.LENGTH_LONG).show();
                        break;
                }*/
            }

        });

    }
}
