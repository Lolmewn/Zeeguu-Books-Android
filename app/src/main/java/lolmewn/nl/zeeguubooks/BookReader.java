package lolmewn.nl.zeeguubooks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import ch.unibe.zeeguulibrary.WebView.ZeeguuWebViewFragment;
import ch.unibe.zeeguulibrary.WebView.ZeeguuWebViewInterface;
import lolmewn.nl.zeeguubooks.tasks.GetBook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;

public class BookReader extends AppCompatActivity implements ZeeguuWebViewFragment.ZeeguuWebViewCallbacks, ZeeguuWebViewInterface.ZeeguuWebViewInterfaceCallbacks{

    private static final String TAG = "BookReader";
    private ViewPager pager;
    private ScreenSlidePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reader);

        this.pager = (ViewPager)findViewById(R.id.pager);
        this.adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(adapter);

        final String token = this.getIntent().getExtras().getString("token");
        final String epubURL = this.getIntent().getExtras().getString("epub_url");
        new GetBook() {
            @Override
            protected void onPostExecute(Book book) {
                Log.d(TAG, "EPUB loading finished: Title=" + book.getTitle());
                displayBook(book);
            }
        }.execute(epubURL, token);
    }

    private void displayBook(Book book) {
        Spine spine = book.getSpine();
        for (int i = 0; i < book.getContents().size(); i++) {
            StringBuilder sb = new StringBuilder();
            Resource res = spine.getResource(i);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                this.adapter.updatePage(i, sb.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        pager.getAdapter().notifyDataSetChanged();

    /*
        String varMySheet = "var mySheet = document.styleSheets[0];";

        String addCSSRule = "function addCSSRule(selector, newRule) {"
                + "ruleIndex = mySheet.cssRules.length;"
                + "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);"

                + "}";

        String insertRule1 = "addCSSRule('html', 'padding: 0px; height: "
                + (getWebView().getMeasuredHeight() / getContext().getResources().getDisplayMetrics().density)
                + "px; -webkit-column-gap: 0px; -webkit-column-width: "
                + getWebView().getMeasuredWidth() + "px;')";


        getWebView().loadUrl("javascript:" + varMySheet);
        getWebView().loadUrl("javascript:" + addCSSRule);
        getWebView().loadUrl("javascript:" + insertRule1);*/
    }

    @Override
    public ZeeguuConnectionManager getZeeguuConnectionManager() {
        return QuickFix.getZeeguuAccount();
    }

    @Override
    public ZeeguuWebViewFragment getWebViewFragment() {
        return null;
    }

    @Override
    public ZeeguuAccount getZeeguuAccount() {
        return QuickFix.getZeeguuAccount().getAccount();
    }

    @Override
    public void showZeeguuLoginDialog(String title, String email) {
        Log.e("BookReader", "A Zeeguu login dialog is requested - please no");
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        private final HashMap<Integer, String> chapters = new HashMap<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ChapterViewer.newInstance(chapters.get(position));
        }

        @Override
        public int getCount() {
            return chapters.size();
        }

        public void updatePage(int idx, String html){
            chapters.put(idx, html);
        }
    }
}
