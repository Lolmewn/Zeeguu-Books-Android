package lolmewn.nl.zeeguubooks.settings;

public interface SettingsCallbacks {
    void displayPreferenceScreen(String key);
    void zeeguuLogout();
    void googleLogout();
    void showGoogleLogoutDialog();
}
