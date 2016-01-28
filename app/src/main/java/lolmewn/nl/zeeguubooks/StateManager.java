package lolmewn.nl.zeeguubooks;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

import lolmewn.nl.zeeguubooks.account.ZeeguuBooksAccount;

/**
 * Created by Lolmewn on 15/01/2016.
 * I definitely hate myself for creating this class as it goes completely against any OOP principle
 * But then again, it's just soooo much easier right now. Screw the rules, I have a StateManager!
 */
public class StateManager {

    private static ZeeguuBooksAccount zeeguuAccount;
    private static GoogleSignInAccount googleAccount;
    private static GoogleApiClient googleApiClient;
    private static BookReader reader;

    public static ZeeguuBooksAccount getZeeguuAccount() {
        return zeeguuAccount;
    }

    public static void setZeeguuAccount(ZeeguuBooksAccount zeeguuAccount) {
        StateManager.zeeguuAccount = zeeguuAccount;
    }

    public static GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    public static void setGoogleAccount(GoogleSignInAccount googleAccount) {
        StateManager.googleAccount = googleAccount;
    }

    public static BookReader getReader() {
        return reader;
    }

    public static void setReader(BookReader reader) {
        StateManager.reader = reader;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        StateManager.googleApiClient = googleApiClient;
    }
}
