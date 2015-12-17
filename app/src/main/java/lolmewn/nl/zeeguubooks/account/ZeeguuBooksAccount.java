package lolmewn.nl.zeeguubooks.account;

import android.app.Activity;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;

/**
 * Created by Lolmewn on 08/12/2015.
 */
public class ZeeguuBooksAccount extends ZeeguuConnectionManager {

    public ZeeguuBooksAccount(Activity activity) {
        super(activity);
    }

    public String getSessionID() {
        return getAccount().getSessionID();
    }

    public boolean isEmailValid(CharSequence email) {
        return getAccount().isEmailValid(email);
    }

    public boolean isLanguageSet() {
        return getAccount().isLanguageSet();
    }

    public boolean isUserInSession() {
        return getAccount().isUserInSession();
    }

    public boolean isUserLoggedIn() {
        return getAccount().isUserLoggedIn();
    }

    public boolean isFirstLogin() {
        return getAccount().isFirstLogin();
    }

    public boolean isMyWordsEmpty() {
        return getAccount().isMyWordsEmpty();
    }

    public String getEmail() {
        return getAccount().getEmail();
    }

    public String getPassword() {
        return getAccount().getPassword();
    }
}
