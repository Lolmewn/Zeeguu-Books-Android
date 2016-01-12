package lolmewn.nl.zeeguubooks.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private final int height, width;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        height = bmImage.getHeight();
        width = bmImage.getWidth();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        String urlStr = urls[0];
        Bitmap img = null;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlStr);
        HttpResponse response;
        try {
            response = (HttpResponse)client.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream inputStream = bufferedEntity.getContent();
            return img = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }



        String urldisplay = urls[0];

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BufferedInputStream buffer = new BufferedInputStream(new URL(urldisplay).openConnection().getInputStream());
            BitmapFactory.decodeStream(buffer,null,options);
            buffer.reset();

            // Calculate inSampleSize
            options.inSampleSize = calculateSampleSize(options, height,width);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(buffer,null,options);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bitmap mIcon11 = null;
        try {
            mIcon11 = BitmapFactory.decodeStream(new URL(urldisplay).openConnection().getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

    public int calculateSampleSize(BitmapFactory.Options options,
                                           int reqWidth, int reqHeight) {

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
}