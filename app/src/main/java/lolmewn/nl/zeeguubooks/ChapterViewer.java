package lolmewn.nl.zeeguubooks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ChapterViewer extends Fragment {

    private static final String TAG = "ChapterViewer";

    private String html;

    static ChapterViewer newInstance(String html) {
        Bundle args = new Bundle();
        args.putString("html", html);
        ChapterViewer activity = new ChapterViewer();
        activity.setArguments(args);
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.chapter_view, container, false);


        if(getArguments() != null){
            WebView view = (WebView) mainView.findViewById(R.id.webview_content);
            this.html = getArguments().getString("html");
            view.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }

        return mainView;
    }
}
