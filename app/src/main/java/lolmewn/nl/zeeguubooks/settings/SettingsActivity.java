package lolmewn.nl.zeeguubooks.settings;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuCreateAccountDialog;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuDialogCallbacks;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuLoginDialog;
import lolmewn.nl.zeeguubooks.GoogleLogin;
import lolmewn.nl.zeeguubooks.MainActivity;
import lolmewn.nl.zeeguubooks.StateManager;
import lolmewn.nl.zeeguubooks.R;
import lolmewn.nl.zeeguubooks.dialogs.GoogleLogoutDialog;
import lolmewn.nl.zeeguubooks.dialogs.ZeeguuBooksLogoutDialog;

public class SettingsActivity extends AppCompatActivity implements
        SettingsCallbacks,
        ZeeguuSettingsFragment.ZeeguuSettingsCallbacks,  // showZeeguuLogoutDialog & getZeeguuConnectionManager
        ZeeguuDialogCallbacks {

    private ZeeguuSettingsFragment zeeguuSettingsFragment;
    private GoogleSettingsFragment googleSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key = getString(R.string.tag_main_settings);
        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager().findFragmentByTag(key);
        if (settingsFragment == null) settingsFragment = new SettingsFragment();

        googleSettingsFragment = (GoogleSettingsFragment) getFragmentManager().findFragmentByTag(getString(R.string.tag_google_settings));
        if (googleSettingsFragment == null) googleSettingsFragment = new GoogleSettingsFragment();

        zeeguuSettingsFragment = (ZeeguuSettingsFragment) getFragmentManager().findFragmentByTag(getString(R.string.tag_zeeguu_settings));
        if (zeeguuSettingsFragment == null) zeeguuSettingsFragment = new ZeeguuSettingsFragment();

        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.content, settingsFragment, key).commit();
    }

    @Override
    public void displayPreferenceScreen(String key) {
        Fragment fragment = null;
        if(key.equals(getString(R.string.pref_google))){
            fragment = googleSettingsFragment;
        }else if(key.equals(getString(R.string.pref_zeeguu))){
            fragment = zeeguuSettingsFragment;
        }
        getFragmentManager().beginTransaction().replace(R.id.content, fragment, key).addToBackStack(getString(R.string.tag_main_settings)).commit();
    }

    @Override
    public void zeeguuLogout() {
        getZeeguuConnectionManager().getAccount().logout();
        // Open login flow and clear back-stack
        Intent loginIntent = new Intent(this, MainActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void googleLogout() {
        GoogleApiClient client = StateManager.getGoogleApiClient();
        client.blockingConnect();
        Auth.GoogleSignInApi.signOut(client);
        // open Google login flow and clear back-stack
        Intent loginIntent = new Intent(this, GoogleLogin.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void showGoogleLogoutDialog() {
        GoogleLogoutDialog dialog = new GoogleLogoutDialog();
        dialog.show(getFragmentManager(), "google_logout");
    }

    @Override
    public ZeeguuConnectionManager getZeeguuConnectionManager() {
        return StateManager.getZeeguuAccount();
    }

    @Override
    public void showZeeguuLoginDialog(String title, String email) {
        ZeeguuLoginDialog dialog = new ZeeguuLoginDialog();
        dialog.setMessage(title);
        dialog.setEmail(email);
        dialog.show(getFragmentManager(), "zeeguu_login");
    }

    @Override
    public void showZeeguuCreateAccountDialog(String message, String username, String email) {
        ZeeguuCreateAccountDialog createAccountDialog = new ZeeguuCreateAccountDialog();
        createAccountDialog.setEmail(email);
        createAccountDialog.setMessage(message);
        createAccountDialog.setUsername(username);
        createAccountDialog.show(getFragmentManager(), "zeeguu_register");
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showZeeguuLogoutDialog() {
        ZeeguuBooksLogoutDialog dialog = new ZeeguuBooksLogoutDialog();
        dialog.show(getFragmentManager(), "zeeguu_logout");
    }
}
