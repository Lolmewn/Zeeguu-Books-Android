package lolmewn.nl.zeeguubooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.unibe.zeeguulibrary.WebView.ZeeguuWebViewFragment;

public class ChapterViewer extends ZeeguuWebViewFragment {

    static ChapterViewer newInstance(String html) {
        Bundle args = new Bundle();
        args.putString("html", html);
        ChapterViewer activity = new ChapterViewer();
        activity.setArguments(args);
        return activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.chapter_view, container, false);

        super.setWebView((WebView) mainView.findViewById(R.id.webview_content));
        super.setTranslationBar((RelativeLayout) mainView.findViewById(R.id.webview_translation_bar));
        super.setCallback((ZeeguuWebViewCallbacks) getActivity());
        super.setProgressBar((ProgressBar) mainView.findViewById(R.id.webview_progress_bar));
        super.setTranslationView((TextView) mainView.findViewById(R.id.webview_translation));
        super.setBookmarkButton((ImageView) mainView.findViewById(R.id.webview_bookmark));

        if(getArguments() != null){
            String html = getArguments().getString("html");
            getWebView().loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }

        return mainView;
    }
}
