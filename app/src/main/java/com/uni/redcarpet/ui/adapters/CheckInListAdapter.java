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
import com.uni.redcarpet.models.CheckIn;

import java.util.ArrayList;


public class CheckInListAdapter extends BaseAdapter{

    private ArrayList<CheckIn> checkIns;
    private String[] types;
    private Context mContext;
    private LayoutInflater mInflater;
    private int sizeOfCommentList;

    public CheckInListAdapter(Context mContext, ArrayList<CheckIn> checkIns){
        this.mContext = mContext;
        this.checkIns = checkIns;
        this.types  = types;
        sizeOfCommentList = checkIns.size();

        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return (sizeOfCommentList);
    }

    @Override
    public Object getItem(int position) {

        return checkIns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        rowView = mInflater.inflate(R.layout.item_check_in_list, parent, false);

        // Get commenter's name
        TextView checkedUserName = (TextView) rowView.findViewById(R.id.tv_commenter_name);

        CheckIn thisCheckIn = (CheckIn) getItem(position);

        checkedUserName.setText(thisCheckIn.checkUserName);
        // Get thumbnail element


        ImageView userPhoto = (ImageView) rowView.findViewById(R.id.commenter_photo);

        if (thisCheckIn.checkUserImage.isEmpty()) {
            Picasso.with(mContext).load(R.drawable.party).placeholder(R.drawable.party).into(userPhoto);
        } else {
            Picasso.with(mContext).load(thisCheckIn.checkUserImage).placeholder(R.drawable.party).into(userPhoto);
        }
        return rowView;

    }
}
