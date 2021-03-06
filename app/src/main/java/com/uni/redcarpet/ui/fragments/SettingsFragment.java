package com.uni.redcarpet.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.User;
import com.uni.redcarpet.ui.activities.MainActivity;
import com.uni.redcarpet.utils.UserUtils;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase database;
    public FirebaseAuth auth;
    private FirebaseUser currentFirebaseUser;
    private UserUtils userUtils;

    EditText userName, etEmailSetting, etaddress;
    TextView tvPhoneSetting, tvGeneralSettings;
    Button saveSettingsButton;
    ImageView profilePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // currentFirebaseUser = MainActivity.currentFirebaseUser;
        userUtils = new UserUtils();

        // Views
        profilePicture = (ImageView) v.findViewById(R.id.profilePicture);
        userName = (EditText) v.findViewById(R.id.etSettingName);
        tvPhoneSetting = (TextView) v.findViewById(R.id.tvPhoneSetting);
        tvGeneralSettings = (TextView) v.findViewById(R.id.general_settings);
        etEmailSetting = (EditText) v.findViewById(R.id.etEmailSetting);

        saveSettingsButton = (Button) v.findViewById(R.id.saveSettingsbtn);

        tvPhoneSetting.setText(currentFirebaseUser.getPhoneNumber());

        /*if (!(currentFirebaseUser.getEmail() == null)){
            etEmailSetting.setText(currentFirebaseUser.getEmail());
        }*/
        if (!(currentFirebaseUser.getEmail() == null)){
            etEmailSetting.setText(currentFirebaseUser.getEmail());
        }else {
            etEmailSetting.setText(MainActivity.getCurrentUserEmail());
        }
        if (!(currentFirebaseUser.getDisplayName() == null)){
            userName.setText(currentFirebaseUser.getDisplayName());
        }
        userName.setGravity(Gravity.CENTER_HORIZONTAL);
        saveSettingsButton.setOnClickListener(this);
        tvGeneralSettings.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();

        switch(v.getId()) {
            // create event button
            case R.id.saveSettingsbtn:

                String l_uid = currentFirebaseUser.getUid();
                String l_email = etEmailSetting.getText().toString();
                String l_phoneNumber = currentFirebaseUser.getPhoneNumber();
                String l_firebaseToken = currentFirebaseUser.getIdToken(true).toString();
                String l_userName = userName.getText().toString();

                if (l_email.equals("") ||
                        l_userName.equals("")
                        ) {

                    new AlertDialog.Builder(getActivity())
                            //.setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Empty field(s)!")
                            .setMessage("Please complete the form before updating data.")
                            .setPositiveButton("Ok", null)
                            .show();
                    break;
                } else {
                    User user = new User(l_uid, l_email, l_phoneNumber, l_firebaseToken, l_userName);

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(l_userName)
                            //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();

                    currentFirebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UPDATE_USER_PROFILE:", "User profile updated.");
                                    }
                                }
                            });
                    currentFirebaseUser.updateEmail(l_email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UPDATE_USER_PROFILE", "User email address updated.");
                                    }
                                }
                            });
                    // userUtils.updateUserProfile(getContext(), currentFirebaseUser, user);

                    Toast.makeText(getContext(), "User profile Updated!", Toast.LENGTH_SHORT).show();
                }
                break;
            // cancel button
            case R.id.tvPhoneSetting:

                Fragment newFragment = new EventsFragment();
                newFragment.setArguments(args);

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.general_settings:
                // args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
                // We need to get back to the list of events in the eventfragment.
                Fragment newFragment1 = new PrivacyFragment();
                newFragment1.setArguments(args);

                fragmentTransaction.replace(R.id.frame, newFragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            default:
        }
    }
}
