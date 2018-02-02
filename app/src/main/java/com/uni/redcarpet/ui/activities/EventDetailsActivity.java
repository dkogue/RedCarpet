package com.uni.redcarpet.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.CheckIn;
import com.uni.redcarpet.models.Comment;
import com.uni.redcarpet.models.Event;
import com.uni.redcarpet.ui.adapters.CommentListAdapter;
import com.uni.redcarpet.utils.Constants;
import com.uni.redcarpet.utils.EventUtil;

import java.util.ArrayList;
import java.util.Collections;


public class EventDetailsActivity extends AppCompatActivity {
    TextView organizer, eventName, month, day, year, description, address;
    EditText etCommentMessage;
    ImageView eventIcon, commenterImage;
    Button buttonPostComment;
    Switch chechInSwitch;

    private RecyclerView recyclerView_comment;
    private ListView mListViewComment;

    private EventUtil eventUtil;
    private FirebaseDatabase database;
    public FirebaseAuth auth;
    private DatabaseReference myRef, specificEventRef, specificCommentRef, specificUserChekInREf;
    private TextView emptyListText;
    private String active_user;
    private String userPhoneNumber;
    private String currentClickedEvent;
    private ArrayList<CheckIn> checkIns;
    private ArrayList<Comment> comments;
    private CommentListAdapter customListAdapter;


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
        eventIcon = (ImageView) findViewById(R.id.imageView2);
        etCommentMessage = (EditText) findViewById(R.id.et_add_comment);
        commenterImage = (ImageView) findViewById(R.id.commenter_photo);
        buttonPostComment = (Button) findViewById(R.id.btn_post_comment);
        chechInSwitch = (Switch) findViewById(R.id.checkinswitch);
        emptyListText = (TextView)findViewById(R.id.empty_list);
        mListViewComment = (ListView) findViewById(R.id.list_view_comment);


        etCommentMessage.setFocusable(false);
        etCommentMessage.setFocusableInTouchMode(true);


        final String[] currEvent = getIntent().getExtras().getStringArray("currEvent");
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

        currentClickedEvent = currEvent[1]+"_"+currEvent[2];


        // *******************later load image from firebase

        eventIcon.setImageResource(R.drawable.party);

        userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        setTitle(currEvent[1]);

        eventUtil = new EventUtil();
        database = FirebaseDatabase.getInstance();

        // We verify if this current user has already checked in for this event or not
        // before continuing

        myRef = database.getReference(Constants.ARG_CHECKINS).child(currentClickedEvent);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userPhoneNumber)){
                    chechInSwitch.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        myRef = database.getReference("Comments");

        specificEventRef = myRef.child(currentClickedEvent);
        specificCommentRef = specificEventRef;

        specificCommentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                specificEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentClickedEvent = currEvent[1]+"_"+currEvent[2];
                        comments = eventUtil.getAllCommentsForSpecificEvent(currentClickedEvent, dataSnapshot);

                        // Sort the comments by timestamp
                        Collections.sort(comments);
                        // events = ej.checkDate(date,events);
                        customListAdapter = new CommentListAdapter(getBaseContext(), comments);
                        customListAdapter.notifyDataSetChanged();
                        // final popAdapter events_by_date = new popAdapter(getContext(),events);

                        if (comments.isEmpty())
                            emptyListText.setVisibility(View.VISIBLE);
                        else
                            emptyListText.setVisibility(View.INVISIBLE);

                        mListViewComment.setAdapter(customListAdapter);


                        setListViewHeightBasedOnChildren(mListViewComment, comments, getBaseContext());


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


        buttonPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCommentOnEvent(currEvent);
                etCommentMessage.getText().clear();

            }
        });

        chechInSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    updateCheckInOutStatus(currEvent, isChecked);
                } else {
                    updateCheckInOutStatus(currEvent, false);
                }
            }
        });
    }

    // Post comment: Saves to firebase and updates the customListAdapter
    private void postCommentOnEvent(String[] currentEvent){
        String commentMessage = etCommentMessage.getText().toString();
        String eventName = currentEvent[1];
        // commenterImage.setImageResource(R.drawable.party);
        String commenterPhotoUrl = "";

        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().isEmpty()) {
            active_user = MainActivity.currentFirebaseUser.getPhoneNumber();
        } else {
            active_user = MainActivity.currentFirebaseUser.getEmail();
        }
        String commenterUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Event current_event = new Event (currentEvent[0],
                "Party",
                currentEvent[4],
                currentEvent[2],
                currentEvent[1]);

        Comment new_comment = new Comment(eventName,
                active_user,
                commenterPhotoUrl,
                commentMessage,
                System.currentTimeMillis());
        EventUtil.saveCommentToFirebase(current_event, new_comment);
        comments.add(new_comment);
    }


    private void updateCheckInOutStatus(String[] currentEvent, Boolean check){
        String eventName = currentEvent[1];
        String checkedUserPhotoUrl = "";

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getDisplayName() != null) {
            active_user = firebaseUser.getDisplayName();
        } else {
            active_user = firebaseUser.getPhoneNumber();
        }
        String userNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        // String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Event current_event = new Event (currentEvent[0],
                "Party",
                currentEvent[4],
                currentEvent[2],
                currentEvent[1]);

        CheckIn new_checkin = new CheckIn(eventName,
                userNumber,
                active_user,
                checkedUserPhotoUrl,
                System.currentTimeMillis());
        if (check){
            EventUtil.saveCheckInStatusToFirebase(current_event,new_checkin);
            // checkIns.add(new_checkin);
        } else {
            myRef = database.getReference(Constants.ARG_CHECKINS);
            myRef = myRef.child(current_event.name+"_"+ current_event.date).child(userNumber);
            myRef.removeValue();

        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView, ArrayList<Comment> comments, Context context) {
        CommentListAdapter commentListAdapter =new CommentListAdapter(context, comments);
        if (commentListAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < commentListAdapter.getCount(); i++) {
            view = commentListAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (commentListAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
