package lolmewn.nl.zeeguubooks.javascript;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Lolmewn on 13/01/2016.
 */
public class JavascriptHandler {

    private final Context mContext;

    public JavascriptHandler(Context c) {
        mContext = c;
    }

    //API 17 and higher required you to add @JavascriptInterface as mandatory before your method.
    @JavascriptInterface
    public void processContent(String aContent)
    {
        //this method will be called from within the javascript method that you will write.
        final String content = aContent;
        Log.d("TAG", "The content of the current page is "+ content);
    }
}