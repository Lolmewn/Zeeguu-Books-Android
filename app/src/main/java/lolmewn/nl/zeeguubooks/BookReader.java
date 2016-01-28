package lolmewn.nl.zeeguubooks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.unibe.zeeguulibrary.Core.ZeeguuAccount;
import ch.unibe.zeeguulibrary.Core.ZeeguuConnectionManager;
import ch.unibe.zeeguulibrary.Dialogs.ZeeguuLoginDialog;
import ch.unibe.zeeguulibrary.WebView.ZeeguuWebViewFragment;
import ch.unibe.zeeguulibrary.WebView.ZeeguuWebViewInterface;
import lolmewn.nl.zeeguubooks.tasks.GetBook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;

public class BookReader extends AppCompatActivity implements ZeeguuWebViewFragment.ZeeguuWebViewCallbacks, ZeeguuWebViewInterface.ZeeguuWebViewInterfaceCallbacks{

    private static final String TAG = "BookReader";
    private ViewPager pager;
    private ScreenSlidePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reader);
        StateManager.setReader(this);

        this.pager = (ViewPager)findViewById(R.id.pager);
        this.adapter = new ScreenSlidePagerAdapter(getFragmentManager());
        this.pager.setAdapter(adapter);

        final String authorization_token = this.getIntent().getExtras().getString("authorization_token");
        final String epubURL = this.getIntent().getExtras().getString("epub_url");
        new GetBook() {
            @Override
            protected void onPostExecute(Book book) {
                Log.d(TAG, "EPUB loading finished: Title=" + book.getTitle());
                displayBook(book);
            }
        }.execute(epubURL, authorization_token);
    }

    private void displayBook(Book book) {
        Spine spine = book.getSpine();
        for (SpineReference ref : spine.getSpineReferences()) {
            StringBuilder sb = new StringBuilder();
            Resource res = ref.getResource();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                this.adapter.updatePage(sb.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        pager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public ZeeguuConnectionManager getZeeguuConnectionManager() {
        return StateManager.getZeeguuAccount();
    }

    @Override
    public ZeeguuWebViewFragment getWebViewFragment() {
        return (ZeeguuWebViewFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
    }

    @Override
    public ZeeguuAccount getZeeguuAccount() {
        return StateManager.getZeeguuAccount().getAccount();
    }

    @Override
    public void showZeeguuLoginDialog(String title, String email) {
        ZeeguuLoginDialog loginDialog = new ZeeguuLoginDialog();
        loginDialog.setMessage(title);
        loginDialog.setEmail(email);
        loginDialog.show(getFragmentManager(), "zeeguu_login");
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        private final List<String> chapters = new ArrayList<>();

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

        public void updatePage(String html){
            chapters.add(html);
        }

    }
}
