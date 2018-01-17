package com.uni.redcarpet.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.Comment;
import com.uni.redcarpet.models.Event;
import com.uni.redcarpet.ui.adapters.CommentListAdapter;
import com.uni.redcarpet.utils.EventUtil;

import java.util.ArrayList;


public class EventDetailsActivity extends AppCompatActivity {
    TextView organizer, eventName, month, day, year, description, address;
    EditText etCommentMessage;
    ImageView eventIcon, commenterImage;
    Button buttonPostComment;
    private RecyclerView recyclerView_comment;
    private ListView mListViewComment;

    private EventUtil eventUtil;
    private FirebaseDatabase database;
    public FirebaseAuth auth;
    private DatabaseReference myRef;
    private TextView emptyListText;
    private String commenter;


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
        emptyListText = (TextView)findViewById(R.id.empty_list);
        mListViewComment = (ListView) findViewById(R.id.list_view_comment);



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

        // *******************later load image from firebase

        eventIcon.setImageResource(R.drawable.party);

        setTitle(currEvent[1]);

        eventUtil = new EventUtil();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Comments");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String current_event = currEvent[1]+"_"+currEvent[2];
                ArrayList<Comment> comments = eventUtil.getAllCommentsForEvent(current_event, dataSnapshot);

                // events = ej.checkDate(date,events);
                final CommentListAdapter list = new CommentListAdapter(getBaseContext(), comments);

                // final popAdapter events_by_date = new popAdapter(getContext(),events);

                if (comments.isEmpty())
                    emptyListText.setVisibility(View.VISIBLE);
                else
                    emptyListText.setVisibility(View.INVISIBLE);

                mListViewComment.setAdapter(list);

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
    }

    private void postCommentOnEvent(String[] currentEvent){
        String commentMessage = etCommentMessage.getText().toString();
        String eventName = currentEvent[1];
        // commenterImage.setImageResource(R.drawable.party);
        String commenterPhotoUrl = "";

        if (FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
            commenter = MainActivity.currentUser.getPhoneNumber();
        } else {
            commenter = MainActivity.currentUser.getEmail();
        }
        String commenterUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Event current_event = new Event (currentEvent[0],
                "Party",
                currentEvent[4],
                currentEvent[2],
                currentEvent[1]);

        Comment new_comment = new Comment(eventName,
                commenter,
                commenterPhotoUrl,
                commentMessage,
                System.currentTimeMillis());
        EventUtil.saveCommentToFirebase(current_event,new_comment);
    }


}
