package com.uni.redcarpet.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.CheckIn;
import com.uni.redcarpet.models.Comment;
import com.uni.redcarpet.ui.adapters.CheckInListAdapter;
import com.uni.redcarpet.ui.adapters.CommentListAdapter;
import com.uni.redcarpet.utils.Constants;
import com.uni.redcarpet.utils.EventUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cyrille on 28/01/2018.
 */

public class CheckInPerEvent  extends Fragment{

    TextView  eventName;
    ImageView eventIcon, commenterImage;
    Button buttonPostComment;
    private RecyclerView recyclerView_comment;
    // private ListView mListViewComment;
    private ListView mListViewCheckIn;

    private EventUtil eventUtil;
    private FirebaseDatabase database;
    public FirebaseAuth auth;
    private DatabaseReference myRef, specificEventRef, specificCommentRef, specificUserChekInREf;
    private TextView emptyListText;
    private String commenter;
    //private String[] currEvent;
    private String currentClickedEvent;
    private ArrayList<Comment> comments;
    private ArrayList<CheckIn> checkIns;

    private CommentListAdapter list;
    private CheckInListAdapter checkInListAdapter;
    // private Event currEvent;
    public Context context;

    public CheckInPerEvent() {
        // Required empty public constructor
    }





    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.check_in_per_event, container, false);
        String[] currEvent = getArguments().getStringArray("currEvent");


        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        eventName = (TextView) view.findViewById(R.id.event_name);
        eventIcon = (ImageView) view.findViewById(R.id.imageView2);
        //commenterImage = (ImageView) findViewById(R.id.commenter_photo);
        emptyListText = (TextView)view.findViewById(R.id.empty_list);
        //mListViewComment = (ListView) view.findViewById(R.id.list_view_check_per_event);
        mListViewCheckIn = (ListView) view.findViewById(R.id.list_view_check_per_event);


        currentClickedEvent = currEvent[1]+ "_" +  currEvent[2];

        String currType = "Party";

        // *******************later load image from firebase

        eventName.setText(currEvent[1]);
        eventIcon.setImageResource(R.drawable.party);

        eventUtil = new EventUtil();
        database = FirebaseDatabase.getInstance();
        // myRef = database.getReference("Comments");
        myRef = database.getReference(Constants.ARG_CHECKINS);

        specificEventRef = myRef.child(currentClickedEvent);


        // Retrieving the checkedIn users while listening on Firebase
        specificUserChekInREf = specificEventRef;
        specificUserChekInREf.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                specificEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // currentClickedEvent = currEvent[1]+"_"+currEvent[2];
                        checkIns = eventUtil.getAllCheckInForSpecificEvent(currentClickedEvent, dataSnapshot);

                        // Sort by timestamp
                        Collections.sort(checkIns);
                        // events = ej.checkDate(date,events);
                        checkInListAdapter = new CheckInListAdapter(getActivity(), checkIns);
                        checkInListAdapter.notifyDataSetChanged();
                        // final popAdapter events_by_date = new popAdapter(getContext(),events);

                        if (checkIns == null)
                            emptyListText.setVisibility(View.VISIBLE);
                        else
                            emptyListText.setVisibility(View.INVISIBLE);

                        mListViewCheckIn.setAdapter(checkInListAdapter);


                        // setListViewHeightBasedOnChildren(mListViewComment, checkIns, getBaseContext());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;

}

}
