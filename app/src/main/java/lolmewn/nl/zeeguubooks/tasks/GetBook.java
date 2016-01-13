package lolmewn.nl.zeeguubooks.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class GetBook extends AsyncTask<String, Void, Book> {

    @Override
    public Book doInBackground(String... params) {
        EpubReader reader = new EpubReader();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + params[1]);
            return reader.readEpub(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
