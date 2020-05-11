package akin.person.nasaimageoftheday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView ivImage;
    List<Bitmap> listLinks;

    public DownloadImageTask(List<Bitmap> listLinks, ImageView ivImage) {
        this.listLinks = listLinks;
        this.ivImage = ivImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
           // System.out.println("ERROR: did not make bitmap");
        } //catch

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        listLinks.add(result);
        ivImage.setImageBitmap(listLinks.get(0));
    }
}