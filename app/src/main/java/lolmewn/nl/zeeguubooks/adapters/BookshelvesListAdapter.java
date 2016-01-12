package lolmewn.nl.zeeguubooks.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import lolmewn.nl.zeeguubooks.R;
import lolmewn.nl.zeeguubooks.tasks.BooksTask;
import lolmewn.nl.zeeguubooks.tasks.TaskResult;
import lolmewn.nl.zeeguubooks.tools.DownloadImageTask;

public class BookshelvesListAdapter extends ArrayAdapter<Bookshelf> {

    private static final String TAG = "BookshelvesListAdapter";

    private final Activity activity;
    private final LayoutInflater inflater;
    private final String email;

    public BookshelvesListAdapter(Activity act, int resource, List<Bookshelf> objects, String email) {
        super(act, resource, objects);
        Log.d(TAG, "Launching...");
        this.activity = act;
        inflater = act.getLayoutInflater();
        this.email = email;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
            view = inflater.inflate(R.layout.bookshelf, null);
        }

        Bookshelf shelf = getItem(position);
        Log.d(TAG, "Adding View for item at pos=" + position + "; name=" + shelf.getTitle() + ";volumes=" + shelf.getVolumeCount());

        TextView title = (TextView)view.findViewById(R.id.shelf_title);
        TextView privacy = (TextView)view.findViewById(R.id.shelf_privacy);
        TextView size = (TextView)view.findViewById(R.id.shelf_size);
        ImageView thumbnail = (ImageView)view.findViewById(R.id.shelf_image);

        title.setText(shelf.getTitle());
        privacy.setText(shelf.getAccess());
        int volumes = shelf.getVolumeCount() == null ? 0 : shelf.getVolumeCount().intValue();
        size.setText(Integer.toString(volumes));
        if(volumes >= 1){
            // get the first book and show its image
            new ThumbnailGetter(activity, email, thumbnail, shelf.getId()).execute();
        }

        return view;
    }

    private class ThumbnailGetter extends BooksTask<Volume>{

        private final ImageView view;
        private final Integer shelfId;

        protected ThumbnailGetter(Activity mActivity, String mEmail, ImageView view, Integer shelfId) {
            super(mActivity, mEmail);
            this.view = view;
            this.shelfId = shelfId;
        }

        @Override
        public void handleResult(TaskResult<Volume> result) {
            if(!result.isError()){
                new DownloadImageTask(view).execute(result.getResult().getVolumeInfo().getImageLinks().getSmallThumbnail());
            }else{
                Log.e("Menu_list", "Failed to retrieve book", result.getError());
            }
        }

        @Override
        public TaskResult<Volume> handle(Void... params) {
            try {
                return new TaskResult<Volume>(
                        getService().mylibrary().bookshelves().volumes().list(Integer.toString(shelfId)).setOauthToken(getOAuth2Token()).execute().getItems().get(0));
            } catch (IOException | GoogleAuthException e) {
                return new TaskResult<Volume>(e);
            }
        }
    }
}
