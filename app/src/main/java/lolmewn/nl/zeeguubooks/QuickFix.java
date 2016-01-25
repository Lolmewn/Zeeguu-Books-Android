package lolmewn.nl.zeeguubooks;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import lolmewn.nl.zeeguubooks.account.ZeeguuBooksAccount;

/**
 * Created by Lolmewn on 15/01/2016.
 * I definitely hate myself for creating this class as it goes completely against any OOP principle
 * But then again, it's just soooo much easier right now. Screw the rules, I have a QuickFix!
 */
public class QuickFix {

    private static ZeeguuBooksAccount zeeguuAccount;
    private static GoogleSignInAccount googleAccount;

    public static ZeeguuBooksAccount getZeeguuAccount() {
        return zeeguuAccount;
    }

    public static void setZeeguuAccount(ZeeguuBooksAccount zeeguuAccount) {
        QuickFix.zeeguuAccount = zeeguuAccount;
    }

    public static GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    public static void setGoogleAccount(GoogleSignInAccount googleAccount) {
        QuickFix.googleAccount = googleAccount;
    }
}
