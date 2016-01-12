package lolmewn.nl.zeeguubooks.tasks;

import android.app.Activity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.services.books.model.Volume;

import java.io.IOException;
import java.util.List;

public abstract class GetMyBooks extends BooksTask<List<Volume>> {

    private final int shelfId;

    protected GetMyBooks(Activity mActivity, String mEmail, int shelfId) {
        super(mActivity, mEmail);
        this.shelfId = shelfId;
    }

    @Override
    public TaskResult<List<Volume>> handle(Void... params) {
        try {
            return new TaskResult<List<Volume>>(
                    getService().mylibrary().bookshelves().volumes().list(Integer.toString(shelfId)).setOauthToken(getOAuth2Token()).execute().getItems()
            );
        } catch (IOException | GoogleAuthException e) {
            return new TaskResult<List<Volume>>(e);
        }
    }
}
