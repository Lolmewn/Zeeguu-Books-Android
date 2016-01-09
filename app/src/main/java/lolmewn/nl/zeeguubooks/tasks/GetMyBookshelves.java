package lolmewn.nl.zeeguubooks.tasks;

import android.app.Activity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.services.books.model.Bookshelf;

import java.io.IOException;
import java.util.List;

public abstract class GetMyBookshelves extends BooksTask<List<Bookshelf>> {

    protected GetMyBookshelves(Activity mActivity, String mEmail) {
        super(mActivity, mEmail);
    }

    @Override
    public TaskResult<List<Bookshelf>> handle(Void... params) {
        try {
            return new TaskResult<List<Bookshelf>>(
                    getService().mylibrary().bookshelves().list().setOauthToken(getOAuth2Token()).execute().getItems()
            );
        } catch (IOException | GoogleAuthException e) {
            return new TaskResult<List<Bookshelf>>(e);
        }
    }
}
