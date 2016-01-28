package lolmewn.nl.zeeguubooks.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import lolmewn.nl.zeeguubooks.StateManager;
import lolmewn.nl.zeeguubooks.R;

public class GoogleSettingsFragment extends PreferenceFragment{

    private SettingsCallbacks callback;
    private Preference googleAccountPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_google);
        googleAccountPreference = findPreference("pref_google_account");
        updateAccount();
    }

    private void updateAccount() {
        googleAccountPreference.setSummary(String.format(getString(R.string.settings_google_account_login), StateManager.getGoogleAccount().getEmail()));
    }

    @Override
     public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        // Open Google logout dialog
        if (preference.getKey() != null && preference.getKey().equals("pref_google_account")) {
            callback.showGoogleLogoutDialog();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the interface is implemented in the container activity
        try {
            callback = (SettingsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ZeeguuSettingsCallbacks");
        }
    }
}
