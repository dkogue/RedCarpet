package com.uni.redcarpet.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uni.redcarpet.R;
import com.uni.redcarpet.ui.adapters.UserListingPagerAdapter;

/**
 * Created by cyrille on 26/01/2018.
 */

public class UserListingFragment extends Fragment {
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;
    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View view = inflater.inflate(R.layout.activity_user_listing, container, false);

        bindViews(view);
        init();


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            // init();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void bindViews(View view) {
        mTabLayoutUserListing = (TabLayout) view.findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) view.findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar

        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
