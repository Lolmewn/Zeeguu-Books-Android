package lolmewn.nl.zeeguubooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuCreateAccountDialog;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuLoginDialog;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuLogoutDialog;
import lolmewn.nl.zeeguubooks.account.ZeeguuBooksAccount;
import lolmewn.nl.zeeguubooks.settings.ZeeguuSettingsFragment;

public class MainActivity extends AppCompatActivity implements ZeeguuConnectionManager.ZeeguuConnectionManagerCallbacks, ZeeguuAccount.ZeeguuAccountCallbacks, ZeeguuSettingsFragment.ZeeguuSettingsCallbacks {

    private static final String TAG = "MainActivity";
    private ZeeguuBooksAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
            }
        });
        account = new ZeeguuBooksAccount(this);
        StateManager.setZeeguuAccount(account);
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
                    Toast.makeText(MainActivity.this, "Email or password is incorrect, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    account.getSessionId(username, password);
                }
            }
        });

        final Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZeeguuCreateAccountDialog(null, null, ((EditText) findViewById(R.id.usernameTextField)).getText().toString());
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
    public ZeeguuConnectionManager getZeeguuConnectionManager() {
        return account;
    }

    @Override
    public void showZeeguuLoginDialog(String title, String email) {
        ZeeguuLoginDialog loginDialog = new ZeeguuLoginDialog();
        loginDialog.setEmail(email);
        loginDialog.setMessage(title);
        loginDialog.show(getFragmentManager(), "zeeguu_login");
    }

    @Override
    public void showZeeguuLogoutDialog() {
        ZeeguuLogoutDialog zeeguuLogoutDialog = new ZeeguuLogoutDialog();
        zeeguuLogoutDialog.show(getFragmentManager(), "zeeguu_logout");
    }

    @Override
    public void showZeeguuCreateAccountDialog(String message, String username, String email) {
        ZeeguuCreateAccountDialog createAccountDialog = new ZeeguuCreateAccountDialog();
        createAccountDialog.setUsername(username);
        createAccountDialog.setMessage(message);
        createAccountDialog.setEmail(email);
        createAccountDialog.show(getFragmentManager(), "zeeguu_register");
    }

    @Override
    public void onZeeguuLoginSuccessful() {
        Intent googleLoginIntent = new Intent(this, GoogleLogin.class);
        startActivity(googleLoginIntent);
    }

    @Override
    public void setTranslation(String translation) {
        Log.d(TAG, "Highlight: " + translation);
        StateManager.getReader().getWebViewFragment().setTranslation(translation);
    }

    @Override
    public void highlight(String word) {
        Log.d(TAG, "Highlight: " + word);
        StateManager.getReader().getWebViewFragment().highlight(word);
    }

    @Override
    public void displayErrorMessage(String error, boolean isToast) {
        Log.d(TAG, error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayMessage(String message) {
        Log.d(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
