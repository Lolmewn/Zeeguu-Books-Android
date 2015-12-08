package lolmewn.nl.zeeguubooks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import lolmewn.nl.zeeguubooks.account.ZeeguuBooksAccount;

public class MainActivity extends AppCompatActivity implements ZeeguuAccount.ZeeguuAccountCallbacks {

    private static final String TAG = "MainActivity";
    private ZeeguuBooksAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = new ZeeguuBooksAccount(this);
        account.load();
        if (!account.isUserLoggedIn()) {
            setContentView(R.layout.activity_login);
            registerLoginViewButtons();
        } else {
            setContentView(R.layout.dialog_zeeguu_create_account);
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
                    account.setEmail(username);
                    account.setPassword(password);

                }
            }
        });
    }

    @Override
    public void notifyDataChanged(boolean myWordsChanged) {

    }

    @Override
    public void notifyLanguageChanged(boolean isLanguageFrom) {

    }

    @Override
    public void highlight(String word) {

    }
}
