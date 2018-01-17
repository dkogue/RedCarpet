package com.uni.redcarpet.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.Comment;

import java.util.ArrayList;


public class CommentListAdapter extends BaseAdapter{

    private ArrayList<Comment> comments;
    private String[] types;
    private Context mContext;
    private LayoutInflater mInflater;
    private int sizeOfCommentList;

    public CommentListAdapter(Context mContext, ArrayList<Comment> comments){
        this.mContext = mContext;
        this.comments = comments;
        this.types  = types;
        sizeOfCommentList = comments.size();

        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return (sizeOfCommentList);
    }

    @Override
    public Object getItem(int position) {

        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        rowView = mInflater.inflate(R.layout.item_comment_event_list, parent, false);

        // Get comment message
        TextView commentMessage = (TextView) rowView.findViewById(R.id.tv_comment);

        // Get commenter's name
        TextView commenterName = (TextView) rowView.findViewById(R.id.tv_commenter_name);

        Comment thisComment = (Comment) getItem(position);

        commenterName.setText(thisComment.commenterName);
        commentMessage.setText(thisComment.commentMessage);
        // Get thumbnail element


        ImageView commenterPhoto = (ImageView) rowView.findViewById(R.id.commenter_photo);

        if (thisComment.commenterImage.isEmpty()) {
            Picasso.with(mContext).load(R.drawable.party).placeholder(R.drawable.party).into(commenterPhoto);
        } else {
            Picasso.with(mContext).load(thisComment.commenterImage).placeholder(R.drawable.party).into(commenterPhoto);
        }
        return rowView;

    }
}
