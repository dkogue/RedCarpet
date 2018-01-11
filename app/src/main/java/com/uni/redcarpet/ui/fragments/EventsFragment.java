package com.uni.redcarpet.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.Event;
import com.uni.redcarpet.ui.activities.EventDetailsActivity;
import com.uni.redcarpet.ui.adapters.listAdapter;
import com.uni.redcarpet.utils.EventUtil;

import java.util.ArrayList;


public class EventsFragment extends Fragment {

    private FloatingActionButton buttonCreateNewEvent;
    private ListView mListViewType;
    private ArrayList<Event> events;
    private EventUtil ej;
    private FirebaseDatabase database;
    private DatabaseReference myRef, typeRef;
    private TextView emptyListText;


    public EventsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        final Context context = getActivity().getApplicationContext();
        final String[] types = new String[] {};

        emptyListText = (TextView)view.findViewById(R.id.empty_list);
        buttonCreateNewEvent = (FloatingActionButton) view.findViewById(R.id.btnCreateNewEvent);

        buttonCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            Fragment newFragment;
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            @Override
            public void onClick(View view) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                newFragment = new CreateEventFragment();

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        mListViewType = (ListView) view.findViewById(R.id.listViewEvents);

        ej = new EventUtil();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Event> events = ej.getAllEvents(dataSnapshot,getActivity());
                // events = ej.checkDate(date,events);
                final listAdapter list = new listAdapter(getContext(),events,types);

                // final popAdapter events_by_date = new popAdapter(getContext(),events);

                if (events.isEmpty())
                    emptyListText.setVisibility(View.VISIBLE);
                else
                    emptyListText.setVisibility(View.INVISIBLE);

                mListViewType.setAdapter(list);

                mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Event event_to_be_edited = (Event) list.getItem(i);
                        String[] event_to_edited_string = new String[8];
                        event_to_edited_string[0] = event_to_be_edited.organizer;
                        event_to_edited_string[1] = event_to_be_edited.name;
                        event_to_edited_string[2] = event_to_be_edited.date;
                        event_to_edited_string[3] = event_to_be_edited.description;
                        event_to_edited_string[4] = event_to_be_edited.address;
                        event_to_edited_string[5] = event_to_be_edited.latitude;
                        event_to_edited_string[6] = event_to_be_edited.longitude;
                        event_to_edited_string[7] = event_to_be_edited.type;

                        Bundle bundle = new Bundle();

                        bundle.putStringArray("currEvent",event_to_edited_string);

                        Intent detail = new Intent(getActivity(), EventDetailsActivity.class);
                        detail.putExtras(bundle);

                        startActivity(detail);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

}
