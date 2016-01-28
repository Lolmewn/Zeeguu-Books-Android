package lolmewn.nl.zeeguubooks;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;

import lolmewn.nl.zeeguubooks.adapters.BookshelvesListAdapter;
import lolmewn.nl.zeeguubooks.adapters.VolumesListAdapter;
import lolmewn.nl.zeeguubooks.tasks.GetMyBooks;
import lolmewn.nl.zeeguubooks.tasks.GetMyBookshelves;
import lolmewn.nl.zeeguubooks.tasks.OpenBook;
import lolmewn.nl.zeeguubooks.tasks.TaskResult;

import java.util.List;

public class BookshelfFragment extends Fragment {

    private static final String TAG = "Bookshelf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        loadBookshelves();
        return view;
    }

    private void loadBookshelves() {
        Log.d(TAG, "Loading bookshelves...");
        new GetMyBookshelves(getActivity(), StateManager.getGoogleAccount().getEmail()) {
            @Override
            public void handleResult(TaskResult<List<Bookshelf>> result) {
                Log.d(TAG, "Results are in!");
                if (result.isError()) {
                    result.getError().printStackTrace();
                } else {
                    Log.d(TAG, "We have " + result.getResult().size() + " bookshelves");
                    // Show dem books!
                    List<Bookshelf> shelves = result.getResult();
                    handleShelves(shelves);
                }
            }
        }.execute();
    }

    private void handleShelves(List<Bookshelf> shelves) {
        if(!isVisible()){
            Log.i(TAG, "We're not visible anymore :c");
            return;
        }
        ListView list = (ListView) getView().findViewById(R.id.books_list);
        final BookshelvesListAdapter adapter = new BookshelvesListAdapter(getActivity(), R.layout.bookshelf, shelves, StateManager.getGoogleAccount().getEmail());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked bookshelf " + position);
                Bookshelf shelf = adapter.getItem(position);
                if (shelf.getVolumeCount() == null || shelf.getVolumeCount() == 0) {
                    Toast.makeText(getActivity(), R.string.no_books, Toast.LENGTH_SHORT).show();
                } else {
                    loadBookshelf(shelf);
                }
            }
        });
    }

    private void loadBookshelf(Bookshelf shelf) {
        new GetMyBooks(getActivity(), StateManager.getGoogleAccount().getEmail(), shelf.getId()) {
            @Override
            public void handleResult(TaskResult<List<Volume>> result) {
                Log.d(TAG, "Volume results are in!");
                if (result.isError()) {
                    result.getError().printStackTrace();
                } else {
                    Log.d(TAG, "We have " + result.getResult().size() + " volumes");
                    // Show dem books!
                    List<Volume> volumes = result.getResult();
                    handleVolumes(volumes);
                }
            }
        }.execute();
    }

    private void handleVolumes(List<Volume> volumes) {
        if(!isVisible()){
            Log.i(TAG, "We're not visible anymore :c");
            return;
        }
        ListView list = (ListView) getView().findViewById(R.id.books_list);
        final VolumesListAdapter adapter = new VolumesListAdapter(getActivity(), R.layout.volume, volumes, StateManager.getGoogleAccount().getEmail());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked volume " + position);
                Volume book = adapter.getItem(position);
                openBook(book);
            }
        });
    }

    private void openBook(Volume book) {
        Log.i(TAG, "Opening book " + book.getVolumeInfo().getTitle());
        final Intent intent = new Intent(getActivity(), BookReader.class);
        intent.putExtra("epub_url", book.getAccessInfo().getEpub().getDownloadLink() + "&key=" + getString(R.string.api_key));
        new OpenBook(getActivity(), StateManager.getGoogleAccount().getEmail(), intent) {
            @Override
            public void handleResult(TaskResult<Intent> result) {
                if(result.isError()){
                    result.getError().printStackTrace();
                }else{
                   startActivity(intent);
                }
            }
        }.execute();
    }

}
