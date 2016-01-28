package lolmewn.nl.zeeguubooks.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import lolmewn.nl.zeeguubooks.R;

/**
 * Created by Lolmewn on 28/01/2016.
 */
public class SettingsFragment extends PreferenceFragment{
    protected SettingsCallbacks settingsCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey() == null)
            return false;

        if (preference.getKey().equals(getString(R.string.pref_zeeguu)))
            settingsCallback.displayPreferenceScreen(getString(R.string.pref_zeeguu));
        else if (preference.getKey().equals(getString(R.string.pref_google)))
            settingsCallback.displayPreferenceScreen(getString(R.string.pref_google));

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        settingsCallback = (SettingsCallbacks) activity;
    }
}
