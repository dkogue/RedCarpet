package com.uni.redcarpet.core.users.add;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uni.redcarpet.R;
import com.uni.redcarpet.models.User;
import com.uni.redcarpet.ui.activities.MainActivity;
import com.uni.redcarpet.utils.Constants;
import com.uni.redcarpet.utils.SharedPrefUtil;


public class AddUserInteractor implements AddUserContract.Interactor {
    private AddUserContract.OnUserDatabaseListener mOnUserDatabaseListener;
    //public User newUser = new User(null,null,null,null,null);

    public AddUserInteractor(AddUserContract.OnUserDatabaseListener onUserDatabaseListener) {
        this.mOnUserDatabaseListener = onUserDatabaseListener;
    }

    @Override
    public void addUserToDatabase(final Context context, final FirebaseUser firebaseUser) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        User user = new User(firebaseUser.getUid(),
                MainActivity.getCurrentUserEmail(),firebaseUser.getPhoneNumber(),
                new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN), MainActivity.getCurrentUsername());


            databaseRef.child(Constants.ARG_USERS).child(firebaseUser.getUid())
                    .setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mOnUserDatabaseListener.onSuccess(context.getString(R.string.user_successfully_added));
                            } else {
                                mOnUserDatabaseListener.onFailure(context.getString(R.string.user_unable_to_add));
                            }
                        }
                    });

    }
}
