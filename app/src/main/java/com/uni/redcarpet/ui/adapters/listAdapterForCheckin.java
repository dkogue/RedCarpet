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
import com.uni.redcarpet.models.Event;

import java.util.ArrayList;


public class listAdapterForCheckin extends BaseAdapter{

    private ArrayList<Event> events;
    private String[] types;
    private Context mContext;
    private LayoutInflater mInflater;
    private int sizeOfTypes;
    private int sizeOfPops;

    public listAdapterForCheckin(Context mContext, ArrayList<Event> events, String[] types){
        this.mContext = mContext;
        this.events = events;
        this.types  = types;
        sizeOfPops = events.size();
        sizeOfTypes = types.length;

        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return (sizeOfPops+sizeOfTypes);
    }

    @Override
    public Object getItem(int position) {
        if (position < sizeOfTypes) return types[position];
        //System.out.println(position+" and "+sizeOfTypes);
        return events.get(position - sizeOfTypes);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        rowView = mInflater.inflate(R.layout.item_event_list_for_checkin, parent, false);

        // Get title element
        TextView eventName = (TextView) rowView.findViewById(R.id.event_title);

        // Get subtitle element
        TextView eventPersons = (TextView) rowView.findViewById(R.id.event_date);

        // Get detail element

        Event thisEvent = (Event) getItem(position);

        eventName.setText(thisEvent.getName());
        eventPersons.setText(thisEvent.date);
        // Get thumbnail element

        ImageView thumbnailImageView = (ImageView) rowView.findViewById(R.id.event_photo);


        Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.party).into(thumbnailImageView);

        return rowView;

    }
}
