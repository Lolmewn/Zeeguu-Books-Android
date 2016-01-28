package lolmewn.nl.zeeguubooks.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class GetBook extends AsyncTask<String, Void, Book> {

    @Override
    public Book doInBackground(String... params) {
        EpubReader reader = new EpubReader();
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            String url = params[0];
            String authorization_token = params[1];
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + authorization_token);
            stream = connection.getInputStream();
            return reader.readEpub(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }
}
