package akin.person.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {
    String apiKey = "&api_key=6i0YPY17QN2uc3ZqdVSFrXieMJ79rpgV8xdSNxqD";
    String hdKey = "&hd=true";
    String link = "https://api.nasa.gov/planetary/apod?date=";

    ImageView ivImage;
    TextView dateTV;
    TextView descTV;
    Button btnLoad;
    Button btnPPause;
    Button btnBack;
    Button btnFrd;
    TextView tvYear;
    TextView tvMonth;
    Timer timer = new Timer();

    List<Bitmap> imgList = new LinkedList<>();
    List<String> dateList = new LinkedList<String>();
    List<String> descList = new LinkedList<String>();

    //List<String> listCont = new LinkedList<>();
    int imageCounter = 0;
    int daysInMonth = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = (ImageView) findViewById(R.id.IVImage);
        dateTV = (TextView) findViewById(R.id.tv_date);
        descTV = (TextView) findViewById(R.id.tv_description);
        descTV.setMovementMethod(new ScrollingMovementMethod());
        btnLoad = (Button) findViewById(R.id.btn_load);
        btnPPause = (Button) findViewById(R.id.btn_pp);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnFrd = (Button) findViewById(R.id.btn_frwd);
        tvYear = (TextView) findViewById(R.id.et_YYYY);
        tvMonth = (TextView) findViewById(R.id.et_MM);


        btnLoad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadImage();
                btnFrd.setEnabled(true);
                btnPPause.setEnabled(true);
                imageCounter = 0;

            } //onClick
        }); //button load

        btnBack.setEnabled(false);
        btnFrd.setEnabled(false);
        btnPPause.setEnabled(false);

        //default image and info
        String nasaLink = "https://api.nasa.gov/planetary/apod?date=2020-05-8&hd=true&api_key=6i0YPY17QN2uc3ZqdVSFrXieMJ79rpgV8xdSNxqD";
        new GetWebContentsTask(ivImage, this.dateTV, descTV, descList, dateList, imgList).execute(nasaLink);



        //moves SlideShow forward 1 day in month
        btnFrd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (imageCounter < daysInMonth - 1) {
                    moveForward();

                } //moves slideshow forward
                if (imageCounter + 1 == daysInMonth - 1) {
                    btnFrd.setEnabled(false);
                } //upper bound of slideshow
                if (imageCounter > 0) {
                    btnBack.setEnabled(true);
                } //enables button

            } //onClick
        }); //button Forward

        //moves SlideShow backward 1 day in month
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (imageCounter > 0) {
                    moveBack();

                } //moves slideshow back
                if (imageCounter < daysInMonth - 1) {
                    btnFrd.setEnabled(true);
                } //upper bound of slideshow
                if (imageCounter == 0) {
                    btnBack.setEnabled(false);
                } //disables button

            } //onClick
        }); //button Backward



        //sets time
        btnPPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (btnPPause.getText().equals("PLAY") || btnPPause.getText().equals("Play")) {
                    btnPPause.setText("PAUSE");
                    btnFrd.setEnabled(false);
                    btnBack.setEnabled(false);


                } else {
                    btnPPause.setText("PLAY");
                    btnFrd.setEnabled(true);
                    btnBack.setEnabled(true);



                }
            } //onClick
        }); //play pause




    } //onCreate


    /**
     * Moves slideshow forward one
     */
    public void moveForward() {
        ivImage.setImageBitmap(imgList.get(imageCounter + 1));
        dateTV.setText(dateList.get(imageCounter + 1));
        descTV.setText(descList.get(imageCounter + 1));
        imageCounter++;

    } //moveForward

    /**
     * Moves slideshow back one
     */
    public void moveBack() {
        ivImage.setImageBitmap(imgList.get(imageCounter - 1));
        dateTV.setText(dateList.get(imageCounter - 1));
        descTV.setText(descList.get(imageCounter - 1));
        imageCounter--;

    } //moveBack


    /**
     * Loads image into image view
     */
    private void loadImage() {
        String year = tvYear.getText().toString();
        String month = tvMonth.getText().toString();
        descList.clear();
        dateList.clear();
        imgList.clear();

        if (year.length() == 4 && Integer.parseInt(year) > 1995 && //valid year
            month.length() == 2 && Integer.parseInt(month) > 0 &&
                Integer.parseInt(month) <= 12) { //valid month
                //finds number of days in valid entry
                daysInMonth = checkDate(year + "-" + month);

                String date = "";
                String urlLink = "";
                String imgLink = "";
                String imgDate = "";
                String imgDesc = "";
                for (int i = 1; i <= daysInMonth; i++) {
                    date += year + "-" + month + "-" + i;
                    urlLink +=  link + date + hdKey + apiKey;
                    getWebContents(urlLink);



                    //resets variables
                    date = "";
                    urlLink = "";
                } //creates links



        } //date after 1995-07

    } //loadImage

    /**
     * Reads the webpage contents Strings.
     * @param link - Url to be searched.
     */
    public void getWebContents(String link) {
        // String contents = new GetWebContentsTask(link).onPostExecute();
        String contents = "input";
        // System.out.println("Inputted Link: " + link);
        AsyncTask webCont = new GetWebContentsTask(ivImage, this.dateTV, descTV, descList, dateList, imgList).execute(link);




        //System.out.println("Contents in getWebContents(): \n" + contents + "\n");

    } //getWebContents

//    /**
//     * Finds and returns the author's description of image.
//     * @param contents - contents of webpage
//     * @return string of image description
//     */
//    public String getImageDesc(String contents) {
//        String desc = "";
//        int start = contents.indexOf("explanation:");
//        start = start + 14;
//        int end = contents.indexOf("\"", start+1);
//        desc = contents.substring(start, end);
//
//        return desc;
//    } //getImageDesc


//    /**
//     * Reads webpage and finds date of image
//     * @param contents - contents of webpage
//     * @return string representing the date
//     */
//    public String getImageDate(String contents) {
//        String date = "";
//        int start = contents.indexOf("date:");
//        start = start + 7;
//        int end = contents.indexOf("\"", start+1);
//        date = contents.substring(start, end);
//
//        return date;
//    } //getImageDate




//    /**
//     * Parses the webpage contents to find image URL.
//     * @param contents - contents to search for URL.
//     * @return URL of the image of the Day.
//     * @throws MalformedURLException
//     */
//    public String getImage(String contents) {
//        String imageLink = "";
//
//
//        return imageLink;
//    } //getImage

    private static int checkDate(String date) {

        String[] localDay = today();
        String[] dateArr = date.split("-");
        int day = 0; //= numDays(date);

        /*
        Limits days searched to current day.
        If year and month match date inputted.
        Sees if local day is date inputted.
        */
        if (localDay[0].equals(dateArr[0]) &&
                localDay[1].equals(dateArr[1])) {

            day = Integer.valueOf(localDay[2]);
        }  else {
            day = numDays(date);
        } //if not current day, then calculates days in month


        return day;
    } //checkDate

    /**
     * Finds todays date as an upper bounds.
     * @return local date
     */
    private static String[] today() {
        LocalDate today = LocalDate.now();
        String todayString = today.toString();
        String[] todayArr = todayString.split("-");

        return todayArr;
    } //today

    /**
     * Determines the number of days in the given month for the year.
     * @param date - YYY/MM format for year and month.
     * @return Number of days for that month.
     */
    private static int numDays(String date) {
        int days = 0;

        String[] splitString = date.split("-");
        LocalDate localDate =
                LocalDate.of(Integer.valueOf(splitString[0]),
                        Integer.valueOf(splitString[1]), 1);

        days = localDate.lengthOfMonth();

        return days;
    } //numDays



} //mainActivity
