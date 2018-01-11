package com.uni.redcarpet.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.uni.redcarpet.R;


public class EventDetailsActivity extends AppCompatActivity {
    TextView organizer, eventName, month, day, year, description, address;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        organizer = (TextView) findViewById(R.id.organizer);
        eventName = (TextView) findViewById(R.id.eventName);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        year = (TextView) findViewById(R.id.year);
        description = (TextView) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.address);
        icon = (ImageView) findViewById(R.id.imageView2);

        String[] currEvent = getIntent().getExtras().getStringArray("currEvent");
        String[] date = currEvent[2].split("-");

        if(date[1].length() == 1)
            date[1] = "0" + date[1];

        if(date[0].length() == 1)
            date[0] = "0" + date[0];

        organizer.setText(currEvent[0]);
        eventName.setText(currEvent[1]);
        month.setText(date[0]);
        day.setText(date[1]);
        year.setText(date[2]);
        address.setText(currEvent[4]);
        description.setText(currEvent[3]);

        String currType = "Party";

        // *******************later load image from firebase
        icon.setImageResource(R.drawable.party);

        setTitle(currEvent[1]);
    }

}
