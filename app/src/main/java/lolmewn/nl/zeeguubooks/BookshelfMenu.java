package lolmewn.nl.zeeguubooks;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;

import java.io.IOException;
import java.util.List;

import lolmewn.nl.zeeguubooks.adapters.BookshelvesListAdapter;
import lolmewn.nl.zeeguubooks.adapters.VolumesListAdapter;
import lolmewn.nl.zeeguubooks.tasks.GetMyBooks;
import lolmewn.nl.zeeguubooks.tasks.GetMyBookshelves;
import lolmewn.nl.zeeguubooks.tasks.TaskResult;
import lolmewn.nl.zeeguubooks.tools.DownloadImageTask;

public class BookshelfMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BookshelfMenu";
    private GoogleSignInAccount acct;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launching BookshelfMenu");
        super.onCreate(savedInstanceState);
        acct = this.getIntent().getExtras().getParcelable("google_account");
        setContentView(R.layout.activity_bookshelf_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set icon, name and email in side-navbar
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        ImageView profilePicture = (ImageView) header.findViewById(R.id.profilePicture);
        new DownloadImageTask(profilePicture).execute(acct.getPhotoUrl().toString());
        TextView username = (TextView) header.findViewById(R.id.user_name);
        username.setText(acct.getDisplayName());
        TextView email = (TextView) header.findViewById(R.id.user_email);
        email.setText(acct.getEmail());
        navigationView.setCheckedItem(R.id.nav_main_menu);

        loadBookshelves();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadBookshelves() {
        Log.d(TAG, "Loading bookshelves...");
        new GetMyBookshelves(this, acct.getEmail()) {
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
        ListView list = (ListView) findViewById(R.id.books_list);
        final BookshelvesListAdapter adapter = new BookshelvesListAdapter(this, R.layout.bookshelf, shelves, acct.getEmail());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked bookshelf " + position);
                Bookshelf shelf = adapter.getItem(position);
                if (shelf.getVolumeCount() == null || shelf.getVolumeCount() == 0) {
                    Toast.makeText(BookshelfMenu.this, R.string.no_books, Toast.LENGTH_SHORT).show();
                } else {
                    loadBookshelf(shelf);
                }
            }
        });
    }

    private void loadBookshelf(Bookshelf shelf) {
        new GetMyBooks(this, acct.getEmail(), shelf.getId()) {
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
        ListView list = (ListView) findViewById(R.id.books_list);
        final VolumesListAdapter adapter = new VolumesListAdapter(this, R.layout.volume, volumes, acct.getEmail());
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bookshelf_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_main_menu) {

        } else if (id == R.id.nav_library) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BookshelfMenu Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://lolmewn.nl.zeeguubooks/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BookshelfMenu Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://lolmewn.nl.zeeguubooks/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
