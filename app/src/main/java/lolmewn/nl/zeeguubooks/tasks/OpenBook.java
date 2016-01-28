package lolmewn.nl.zeeguubooks.tasks;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;

public abstract class OpenBook extends BooksTask<Intent> {

    private final Intent intent;

    protected OpenBook(Activity mActivity, String mEmail, Intent intent) {
        super(mActivity, mEmail);
        this.intent = intent;
    }

    @Override
    public TaskResult<Intent> handle(Void... params) {
        try {
            intent.putExtra("authorization_token", getOAuth2Token());
        } catch (IOException | GoogleAuthException e) {
            return new TaskResult<Intent>(e);
        }
        return new TaskResult<Intent>(intent);
    }
}
