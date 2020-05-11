package akin.person.nasaimageoftheday;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetWebContentsTask extends AsyncTask<String, String, String> {
     String contents;
  //  String url;
    List<String> listDesc;
    List<String> listDates;
    List<Bitmap> listLinks; //bitmapLinks

    ImageView imageView;
    TextView date;
    TextView description;

    public GetWebContentsTask(ImageView iv, TextView tv_date, TextView tv_cont,
                              List<String> content, List<String> dates, List<Bitmap> imageLinks) {
        this.imageView = iv;
        this.date = tv_date;
        this.description = tv_cont;

        this.listDesc = content;
        this.listDates = dates;
        this.listLinks = imageLinks;
//        this.listDesc.clear();
//        this.listDates.clear();
//        this.listLinks.clear();

    } //constructor

    protected String doInBackground(String... urls) {
        String cont = "";

        BufferedReader br = null;

        try {
            URL url = new URL(urls[0]);

            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            } //while

            cont = sb.toString().trim();

            if (br != null) {
                br.close();
            } //if

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //System.out.println("MalformedURL");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //	System.out.println("IOException");
        } //try-catch



        return cont;
    } //doInBackground





    @Override
    protected void onPostExecute(String content) {


        String imgLink = getImage(content);

        new DownloadImageTask(this.listLinks, imageView).execute(imgLink); //adds image bitmap to list
        listDesc.add(getImageDesc(content));
        listDates.add(getImageDate(content));
        this.description.setText(listDesc.get(0)); //sets description
        this.date.setText(listDates.get(0)); //sets date


       // listLinks.add(imgLink);
    } //onPostExecute

    /**
     * Takes webpage contents and finds image Description.
     * @param contents - contents of webpage.
     * @return String of image Description.
     */
    public String getImageDesc(String contents) {
        String desc = "";

        int start = contents.indexOf("explanation") + 14;
        int end = contents.indexOf("\"", start + 1);
        desc = contents.substring(start, end);

        return desc;
    } //getImageDesc

    /**
     * Parses the webpage contents to find image URL.
     * @param contents - contents to search for URL.
     * @throws MalformedURLException
     */
    public String getImage(String contents) {
        String imageLink = "";

        //parses contents for HDURL
        int start = contents.indexOf("hdurl") + 8;
        int end = contents.indexOf("\"", start + 1);
        imageLink = contents.substring(start, end);

        return imageLink;

    } //getImage


    /**
     * Reads webpage and finds date of image
     * @param contents - contents of webpage
     * @return string representing the date
     */
    public String getImageDate(String contents) {
        String date = "";
        int start = contents.indexOf("date") + 7;
        int end = contents.indexOf("\"", start+1);

        date = contents.substring(start, end);

        return date;
    } //getImageDate

} //getWebContentsTask