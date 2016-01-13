package lolmewn.nl.zeeguubooks.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;

import java.io.IOException;

import lolmewn.nl.zeeguubooks.R;
import lolmewn.nl.zeeguubooks.tools.TokenGetter;

public abstract class BooksTask<T> extends AsyncTask<Void, Void, TaskResult<T>>{

    private final Activity mActivity;
    private final String mEmail, mScope;

    protected BooksTask(Activity mActivity, String mEmail) {
        this.mActivity = mActivity;
        this.mEmail = mEmail;
        this.mScope = mActivity.getString(R.string.books_scope);
    }

    public abstract void handleResult(TaskResult<T> result);

    public abstract TaskResult<T> handle(Void... params);

    @Override
    protected TaskResult<T> doInBackground(Void... params) {
        TaskResult<T> res = handle(params);
        return res;
    }

    @Override
    protected void onPostExecute(TaskResult<T> tTaskResult) {
        handleResult(tTaskResult);
    }

    public Books getService() throws IOException, GoogleAuthException {
        return new Books.Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), null)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(mActivity.getString(R.string.api_key)))
                .setApplicationName("Zeeguu Books")
                .build();
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String getOAuth2Token() throws IOException, GoogleAuthException {
        return TokenGetter.getOAuth2Token(mActivity, mEmail, mScope);
    }

}
