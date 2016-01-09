package lolmewn.nl.zeeguubooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import lolmewn.nl.zeeguubooks.account.ZeeguuBooksAccount;

public class MainActivity extends AppCompatActivity implements ZeeguuConnectionManager.ZeeguuConnectionManagerCallbacks, ZeeguuAccount.ZeeguuAccountCallbacks {

    private static final String TAG = "MainActivity";
    private ZeeguuBooksAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = new ZeeguuBooksAccount(this);
        if(!account.isUserLoggedIn()){
            setContentView(R.layout.activity_login);
            registerLoginViewButtons();
        } else {
            Intent googleLoginIntent = new Intent(this, GoogleLogin.class);
            startActivity(googleLoginIntent);
        }


    }

    private void registerLoginViewButtons() {
        final Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.usernameTextField)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordTextField)).getText().toString();
                Log.d(TAG, String.format("Login button clicked, username: %s, password: %s", username, password));
                if (!account.isEmailValid(username)) {
                    // ERROR HANDLING
                } else {
                    account.getSessionId(username, password);
                }
            }
        });
    }

    @Override
    public void notifyDataChanged(boolean myWordsChanged) {
        Log.d(TAG, "DChange");
    }

    @Override
    public void notifyLanguageChanged(boolean isLanguageFrom) {
        Log.d(TAG, "langChanged");
    }

    @Override
    public void bookmarkWord(String bookmarkID) {
        Log.d(TAG, "Bookmark: " + bookmarkID);
    }

    @Override
    public void setDifficulties(ArrayList<HashMap<String, String>> difficulties) {
        Log.d(TAG, "setDiff: ");
    }

    @Override
    public void setLearnabilities(ArrayList<HashMap<String, String>> learnabilities) {
        Log.d(TAG, "setLearn: ");
    }

    @Override
    public void setContents(ArrayList<HashMap<String, String>> contents) {
        Log.d(TAG, "setContents: ");
    }

    @Override
    public void showZeeguuLoginDialog(String title, String email) {
        Log.d(TAG, "showLoginDialog: ");
    }

    @Override
    public void showZeeguuCreateAccountDialog(String message, String username, String email) {
        Log.d(TAG, "showCreateAccDialog: ");
    }

    @Override
    public void onZeeguuLoginSuccessful() {
        Intent googleLoginIntent = new Intent(this, GoogleLogin.class);
        startActivity(googleLoginIntent);
    }

    @Override
    public void setTranslation(String translation) {
        Log.d(TAG, "Highlight: " + translation);
    }

    @Override
    public void highlight(String word) {
        Log.d(TAG, "Highlight: " + word);
    }

    @Override
    public void displayErrorMessage(String error, boolean isToast) {
        Log.d(TAG, error);
        ((TextView)findViewById(R.id.messageField)).setText(error);
    }

    @Override
    public void displayMessage(String message) {
        Log.d(TAG, message);
        ((TextView)findViewById(R.id.messageField)).setText(message);
    }
}
