package com.uni.redcarpet.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uni.redcarpet.R;


public class DeleteAccountFragment extends Fragment implements View.OnClickListener {


    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.delete_account_fragment, container, false);

        Button cancel_btn = (Button) v.findViewById(R.id.button5);
        Button delete_btn = (Button) v.findViewById(R.id.delete_account_btn);

        cancel_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // cancel button
            case R.id.button5:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putBoolean("hasLoggedIn", PrivacyFragment.getLoggedIn());
                Fragment newFragment = new PrivacyFragment();
                newFragment.setArguments(args);

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            // save changes button
            case R.id.delete_account_btn:
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        alertDialog.setTitle("Delete Account")
                        .setMessage("Delete your account?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                removeUserFromFirebase();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("DELETE USER ACCOUNT", "User account deleted.");
                                                }
                                            }
                                        });
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                break;
            default:
        }
    }

    public void removeUserFromFirebase(){
        FirebaseDatabase database;
        DatabaseReference myRef;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef = myRef.child(uid);
        myRef.removeValue();



        myRef = database.getReference("UserEvents");
        myRef = myRef.child(uid);
        myRef.removeValue();
    }
}
