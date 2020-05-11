package akin.person.nasaimageoftheday;


import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetWebContentsTaskDEF extends AsyncTask<String, String, String> {
     String contents;
  //  String url;
    List<String> listCont;
    List<String> listDates;
    List<String> listLinks;

    ImageView imageView;
    TextView date;
    TextView description;

    public GetWebContentsTaskDEF(ImageView iv, TextView tv_date, TextView tv_cont) {
        this.imageView = iv;
        this.date = tv_date;
        this.description = tv_cont;


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
        System.out.println("Initializing contents in task");

        this.date.setText(getImageDate(content));
        setImage(content);
        this.description.setText(getImageDesc(content));

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
    public void setImage(String contents) {
        String imageLink = "";

        //parses contents for HDURL
        int start = contents.indexOf("hdurl") + 8;
        int end = contents.indexOf("\"", start + 1);
        imageLink = contents.substring(start, end);


        //makes bitmap for imageLink HDURL
        //sets bitmap to imageView
        AsyncTask imgBitmap = new DownloadImageTaskDEF(this.imageView).execute(imageLink);
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