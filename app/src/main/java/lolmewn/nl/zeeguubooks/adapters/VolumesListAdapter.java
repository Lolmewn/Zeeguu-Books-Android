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

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;

import java.util.List;

import lolmewn.nl.zeeguubooks.R;
import lolmewn.nl.zeeguubooks.tools.DownloadImageTask;

/**
 * Created by Lolmewn on 11/01/2016.
 */
public class VolumesListAdapter extends ArrayAdapter<Volume> {

    private static final String TAG = "VolumeListAdapter";

    private final Activity activity;
    private final LayoutInflater inflater;
    private final String email;

    public VolumesListAdapter(Activity context, int resource, List<Volume> objects, String email) {
        super(context, resource, objects);
        this.activity = context;
        this.inflater = activity.getLayoutInflater();
        this.email = email;
    }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "Adding View for item at pos=" + position);
        View view = convertView;
        if(convertView == null){
            view = inflater.inflate(R.layout.volume, null);
        }

        Volume shelf = getItem(position);

        TextView title = (TextView)view.findViewById(R.id.volume_title);
        TextView author = (TextView)view.findViewById(R.id.volume_author);
        TextView lang = (TextView)view.findViewById(R.id.volume_lang);
        TextView size = (TextView)view.findViewById(R.id.volume_progress);
        ImageView thumbnail = (ImageView)view.findViewById(R.id.volume_image);

        title.setText(shelf.getVolumeInfo().getTitle());
        author.setText(shelf.getVolumeInfo().getAuthors().get(0)); // TODO: Give all authors credit, not just the first
        lang.setText(shelf.getVolumeInfo().getLanguage());
        size.setText("0/" + shelf.getVolumeInfo().getPageCount());
        new DownloadImageTask(thumbnail).execute(shelf.getVolumeInfo().getImageLinks().getThumbnail());

        return view;
    }
}
