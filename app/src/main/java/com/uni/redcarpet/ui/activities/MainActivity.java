package com.uni.redcarpet.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.uni.redcarpet.R;
import com.uni.redcarpet.core.users.add.AddUserContract;
import com.uni.redcarpet.core.users.add.AddUserPresenter;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, AddUserContract.View {


    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth myAuthentication;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    // NEW
    private TextView myTextAppName;
    private TextView myTextStatus;
    private TextView myTextPhoneVerifTitle;
    private TextView myTextPhoneVerifMsg;
    private TextView myTextPhoneSuccess;


    // NEW================================
    private EditText myETextPhoneNoField;
    private EditText myETextVerifCodeField;


    // NEW =================================
    private Button myCreateAccButton;
    private Button myVerifButton;
    private Button myResendCodeButton;

    //NEW =================================

    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout createaccount;

    private AddUserPresenter mAddUserPresenter;
    public static FirebaseUser currentFirebaseUser;
    private static String currentUsername;
    private static String currentUserEmail;
    private static Boolean isInDatabase = false;

    public static FirebaseUser currentUser;
    public View container;
    final Context context = this;

    ProgressBar progressBar;

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        MainActivity.currentUsername = currentUsername;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static void setCurrentUserEmail(String currentUserEmail) {
        MainActivity.currentUserEmail = currentUserEmail;
    }

    public static Boolean getIsInDatabase() {
        return isInDatabase;
    }

    public static void setIsInDatabase(Boolean isInDatabase) {
        MainActivity.isInDatabase = isInDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_all_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBarAll);

        // We assign views
        myTextStatus = (TextView) findViewById(R.id.status);
        myTextAppName = (TextView) findViewById(R.id.appname);

        myETextPhoneNoField = (EditText) findViewById(R.id.input_phonenumber);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
       container = getLayoutInflater().inflate(R.layout.activity_main_all_popup,null);

        myTextPhoneSuccess = (TextView) container.findViewById(R.id.detail);
        myTextAppName = (TextView) container.findViewById(R.id.appname);
        myTextPhoneVerifMsg = (TextView) container.findViewById(R.id.very_message);
        myTextPhoneVerifTitle = (TextView) container.findViewById(R.id.phonverifi);
        myETextVerifCodeField = (EditText) container.findViewById(R.id.input_verificationcode);

        myVerifButton = (Button) container.findViewById(R.id.btn_verify_popup);
        myVerifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = myETextVerifCodeField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    myETextVerifCodeField.setError("Code cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

        myResendCodeButton = (Button) container.findViewById(R.id.btn_resend_popup);
        myResendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(myETextPhoneNoField.getText().toString(), mResendToken);
            }
        });

        myCreateAccButton = (Button) findViewById(R.id.btn_create);

        // We add click listeners to the buttons

        myCreateAccButton.setOnClickListener(this);

        // START initialize_auth
        myAuthentication = FirebaseAuth.getInstance();
        currentFirebaseUser = myAuthentication.getCurrentUser();

        if (currentFirebaseUser != null){
            if ((currentFirebaseUser.getEmail() != null)) {
                setCurrentUserEmail(currentFirebaseUser.getEmail());
            }
        } else {
            setCurrentUserEmail("");
        }

        if (currentFirebaseUser != null){
            if ((currentFirebaseUser.getDisplayName() != null)) {
                setCurrentUsername(currentFirebaseUser.getDisplayName());
            }
        } else {
            setCurrentUsername("");
        }

        updateUI(currentFirebaseUser);

        // END initialize_auth

        // Initialize phone auth callbacks
        // START phone_auth_callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback is created to be invoked in two cases:
                // 1 - Instant verification. Where verification is don without
                // the need to enter the verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // START_EXCLUDE silent
                mVerificationInProgress = false;
                // END_EXCLUDE

                // START_EXCLUDE silent
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Callback invoked in instance of an invalid request for verification
                // such as wrong phone number format.
                Log.w(TAG, "onVerificationFailed", e);
                // START_EXCLUDE silent
                mVerificationInProgress = false;
                // END_EXCLUDE

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // START_EXCLUDE
                    // mPhoneNumberField.setError("Invalid phone number.");
                    myETextPhoneNoField.setError("Invalid phone number.");
                    // END_EXCLUDE
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // START_EXCLUDE
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // END_EXCLUDE
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // END_EXCLUDE
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                //START_EXCLUDE
                // Update UI
                updateUI(STATE_CODE_SENT);
                //END_EXCLUDE
            }
        };
        //END phone_auth_callbacks

    }

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.



        //START_EXCLUDE
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(myETextPhoneNoField.getText().toString());
        }
        //END_EXCLUDE
    }
    // END on_start_check_user

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // START start_phone_auth
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // END start_phone_auth

        mVerificationInProgress = true;
        myTextStatus.setVisibility(View.INVISIBLE);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // START verify_with_code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // END verify_with_code
        signInWithPhoneAuthCredential(credential);
    }

    // START resend_verification
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // END resend_verification

    // START sign_in_with_phone
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        myAuthentication.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // END_EXCLUDE
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // START_EXCLUDE silent
                                myETextVerifCodeField.setError("Invalid code.");
                                //END_EXCLUDE
                            }
                            //START_EXCLUDE silent
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            //END_EXCLUDE
                        }
                    }
                });
    }
    //END sign_in_with_phone

    private void signOut() {
        myAuthentication.signOut();
        updateUI(STATE_INITIALIZED);
    }

    // Update the user interface depending on the call

    private void updateUI(int uiState) {
        updateUI(uiState, myAuthentication.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        mAddUserPresenter = new AddUserPresenter(this);
        switch (uiState) {
            case STATE_INITIALIZED:

                //break;
            case STATE_SIGNIN_FAILED:
                enableViews(myTextStatus);
                myTextStatus.setText(R.string.status_sign_in_failed);
                // No op, handled by sign-in check

                progressBar.setVisibility(View.INVISIBLE);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np op, handled by sign-in check
                mAddUserPresenter.addUser(getBaseContext().getApplicationContext(), myAuthentication.getCurrentUser());

                myTextStatus.setText(R.string.signed_in);
                break;
        }

        if (user == null) {
            // Signed out
            // Adjust the views accordingly

           // myTextStatus.setText(R.string.signed_out);;
        } else {
            // Signed in
            // Adjust the views accordingly
            /*Intent intent = new Intent(this, Hallfinder.class);
            startActivity(intent);*/
            finish();
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = myETextPhoneNoField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            myETextPhoneNoField.setError("Invalid phone number.");
            //mPhoneNumberField.setTextColor(Color.parseColor("#ff1744"));
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            v.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                if (!validatePhoneNumber()) {
                    return;
                }

                // hide keyboard start
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // hide keyboard end


                //myTextStatus.setText("Authenticating....!");
                progressBar.setVisibility(View.VISIBLE);
                startPhoneNumberVerification(myETextPhoneNoField.getText().toString());

                //layout inflater

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Phone Verification");
                alertDialogBuilder.setView(container)
                                .setCancelable(true)
                                .setMessage("Enter the verification code");
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                LayoutInflater inflater = getLayoutInflater();
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            break;
            case R.id.btn_verify_popup:
                String code = myETextVerifCodeField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    myETextVerifCodeField.setError("Code cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btn_resend_popup:
                resendVerificationCode(myETextPhoneNoField.getText().toString(), mResendToken);
                break;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddUserSuccess(String message) {
        // mProgressDialog.dismiss();
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        HomeActivity.startActivity(getBaseContext(), Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onAddUserFailure(String message) {
        // mProgressDialog.dismiss();
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}

