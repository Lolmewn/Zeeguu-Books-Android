package lolmewn.nl.zeeguubooks;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.services.books.model.Bookshelf;

import java.util.List;

import lolmewn.nl.zeeguubooks.adapters.BookshelvesListAdapter;
import lolmewn.nl.zeeguubooks.tasks.GetMyBookshelves;
import lolmewn.nl.zeeguubooks.tasks.TaskResult;
import lolmewn.nl.zeeguubooks.tools.DownloadImageTask;

public class BookshelfMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "BookshelfMenu";
    private GoogleSignInAccount acct;

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
        list.setAdapter(new BookshelvesListAdapter(this, R.layout.bookshelf, shelves, acct.getEmail()));
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
}
