package com.cvapplication.kelevnor.mylocations.REST;

import android.os.AsyncTask;
import android.util.Log;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.WeatherWrap;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mariossifalakis on 2/23/16.
 */
public class REST_getWeather extends AsyncTask<Void, Integer, Void> {
    boolean completedCall = false;
    String response_passed = "";
    OnAsyncResult onAsyncResult;
    String woeid;
    /**
     * @author Marios Sifalakis
     * Class Constructor; Executes a login call
     * ONE DIRECT CALL
     */
    public REST_getWeather(String woeid){
        this.woeid = woeid;
    }

    /**
     * @author Marios Sifalakis
     * @return "VOID"
     *
     * Async task to execute REST Methods (Removes NetworkOnMainThread Exception)
     * It is executed every time a constructor is instantiated
     */

    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }


    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("https://www.metaweather.com/api/location/"+woeid+"/");
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response_passed = GETurlConnectionInputStream(urlConnection);
                Log.e("response_passed", response_passed);

                completedCall= true;
            }
            else{
                response_passed = GETurlConnectionErrorStream(urlConnection);
                completedCall = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void v) {
        if(completedCall){
            onAsyncResult.onResultSuccess(1, response_passed);
        }
        else {
            onAsyncResult.onResultFail(0, response_passed);
        }
    }

    @Override
    protected void onPreExecute() {
    }


    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, String message);
        void onResultFail(int resultCode, String errorMessage);
    }

    public static String GETurlConnectionInputStream(HttpURLConnection urlConnection){
        byte [] buffer = new byte[8192];
        StringBuilder builder = new StringBuilder();
        String response = "";
        int read;

        try {
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            while ((read = is.read(buffer)) != -1)
            {
                builder.append(new String(buffer, 0, read, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("INPUT STREAM", builder.toString());

        response = builder.toString();

        return response;
    }

    public static String GETurlConnectionErrorStream(HttpURLConnection urlConnection){

        byte [] buffer = new byte[8192];
        StringBuilder builder = new StringBuilder();
        String response = "";
        int read;

        try {
            InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
            while ((read = is.read(buffer)) != -1)
            {
                builder.append(new String(buffer, 0, read, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("ERROR STREAM", builder.toString());

        response = builder.toString();

        return response;

    }


}