package lolmewn.nl.zeeguubooks.tools;

import android.app.Activity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

public class TokenGetter {

    public static String getOAuth2Token(Activity act, String email, String scopes) throws GoogleAuthException, IOException {
        return GoogleAuthUtil.getToken(act, email, scopes);
    }
}
