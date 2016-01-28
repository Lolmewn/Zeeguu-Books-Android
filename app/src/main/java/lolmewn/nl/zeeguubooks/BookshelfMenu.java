package lolmewn.nl.zeeguubooks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import ch.unibe.zeeguulibrary.MyWords.MyWordsFragment;
import lolmewn.nl.zeeguubooks.settings.SettingsActivity;
import lolmewn.nl.zeeguubooks.tools.DownloadImageTask;

public class BookshelfMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyWordsFragment.ZeeguuFragmentMyWordsCallbacks {

    private static final String TAG = "BookshelfMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launching BookshelfMenu");
        super.onCreate(savedInstanceState);
        GoogleSignInAccount acct = this.getIntent().getExtras().getParcelable("google_account");
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
        if(acct.getPhotoUrl() != null){
            new DownloadImageTask(profilePicture).execute(acct.getPhotoUrl().toString());
        }
        TextView username = (TextView) header.findViewById(R.id.user_name);
        username.setText(acct.getDisplayName());
        TextView email = (TextView) header.findViewById(R.id.user_email);
        email.setText(acct.getEmail());
        navigationView.setCheckedItem(R.id.nav_library);


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.menu_frame_layout, new BookshelfFragment())
                .commit();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_bookmarks) {
            fragment = new MyWordsFragment();
        } else if (id == R.id.nav_library) {
            fragment = new BookshelfFragment();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.menu_frame_layout, fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ZeeguuConnectionManager getZeeguuConnectionManager() {
        return StateManager.getZeeguuAccount();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openUrlInBrowser(String URL) {
        // not sure what this is about - it was required for a callback... I think I'll just ignore it.
    }
}
