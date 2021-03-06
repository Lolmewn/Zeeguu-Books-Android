package lolmewn.nl.zeeguubooks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

/**
 * A login screen that offers login via email/password.
 */
public class GoogleLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{

    private static final String TAG = "GoogleLoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        Log.d(TAG, "onCreate()");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/books"))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        StateManager.setGoogleApiClient(mGoogleApiClient);

        mGoogleApiClient.connect();

        findViewById(R.id.sign_in_button).setOnClickListener(this);


    }

    @Override
     public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                    if (!googleSignInResult.isSuccess()) {
                        GoogleLogin.this.signIn();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with requestCode " + requestCode);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, acct.getDisplayName() + " (" + acct.getEmail() + ") logged in (" + acct.getId() + ")");
            StateManager.setGoogleAccount(acct);
            Intent bookshelfMenu = new Intent(this, BookshelfMenu.class);
            bookshelfMenu.putExtra("google_account", acct);
            startActivity(bookshelfMenu);
        } else {
            Log.d(TAG, "Signed out");
            // Signed out, show unauthenticated UI.
        }
    }

    private void signIn() {
        Log.d(TAG, "Starting sign in intent");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection to GPlay services failed");
        showErrorDialogAndQuit("The app cannot communicate with the Google API and can therefore not load your books.\n" +
                "Please check your internet connection or try again later.");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");
        signIn();
    }

    private void showProgressDialog() {
        Log.d(TAG, "Showing progress dialog");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        Log.d(TAG, "Hiding progress dialog");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    /*
     * Found here: http://stackoverflow.com/questions/10198904/how-to-display-an-error-dialog-and-quit-in-the-middle-of-a-loading-dialog
     */
    public void showErrorDialogAndQuit(final String message) {
        Log.d(TAG, "Showing error and quit using message " + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog aDialog = new AlertDialog.Builder(GoogleLogin.this).setMessage(
                        message
                ).setTitle("Error")
                        .setNeutralButton("Close", new AlertDialog.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                // Exit the application.
                                finish();
                            }
                        }).create();
                aDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // Disables the back button.
                        return true;
                    }

                });
                aDialog.show();
            }
        });
    }




    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GPlay services");
        // Connected to Google Play services!
        // The good stuff goes here.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection to GPlay services suspended: " + cause);
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }
}

