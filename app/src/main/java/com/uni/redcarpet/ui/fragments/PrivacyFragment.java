package com.uni.redcarpet.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uni.redcarpet.R;
import com.uni.redcarpet.ui.adapters.settingsAdapter;

public class PrivacyFragment extends Fragment {

    private ListView mListViewType;

    private int sizeOfTypes;

    private Activity acitivity;


    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.list, container, false);


        final Context context = getActivity().getApplicationContext();


        mListViewType = (ListView) v.findViewById(R.id.listViewType);


        final String[] types = new String[]{"Manage events","Change phone number",  "Data & Sync", "Delete account"};
        final String[] guest_types = new String[]{"Change phone number", "Data & Sync", "Delete Account", "About"};

        sizeOfTypes = getLoggedIn() ? types.length : guest_types.length;


        settingsAdapter typesAdapter = new settingsAdapter(getContext(), getLoggedIn() ? types : guest_types);


        mListViewType.setAdapter(typesAdapter);
        mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tabName = getLoggedIn() ? types[position] : guest_types[position];
                Fragment newFragment;
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                switch (tabName) {
                    case "Manage events":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new ManageEvent();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Change phone number":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new AccountManagementFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Delete account":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new DeleteAccountFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Data & Sync":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new DataFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                }


            }

        });


        return v;
    }

    public static boolean getLoggedIn() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (!(firebaseUser.getPhoneNumber().equals("+352661268201"))){
                return false;
            } else {
                return true;
            }
        }


}
