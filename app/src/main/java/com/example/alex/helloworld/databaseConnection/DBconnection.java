package com.example.alex.helloworld.databaseConnection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.alex.helloworld.DisplayWeekActivity.DisplayWeekActivity;
import com.example.alex.helloworld.Sport;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agemcipe on 09.11.16.
 */

/**
 * parent class for all database connections
 */

public class DBconnection extends AsyncTask<String, String, String> {
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;
    private static final String BASE_URL = "sportsm8.bplaced.net/MySQLadmin/include";
    public static final String DEBUG_TAG = "some clever Debug tag:";
    private AsyncResponse delegate=null;

    private HttpURLConnection conn;
    private String result;

    public DBconnection(AsyncResponse asyncResponse){
        delegate=asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        //this method will be running on the UI thread

    }


    protected void onPostExecute(String success) {
        try {
            delegate.processFinish(result);
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getInput() {
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder successString = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                successString.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        result = successString.toString();
    }



    @Override
    protected String doInBackground(String... params) {

        try {
            //request data from server via http get request
            URL url = new URL(buildFinalURL(params[0]));
            System.out.print(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //Append parameters so they can be written


            Uri.Builder builder = new Uri.Builder();

            for(int i =1;i<params.length;i+=2) {
                builder.appendQueryParameter(params[i],params[i+1]);
            }
            String query = builder.build().getEncodedQuery();

            //Open connection for sending data

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            getInput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }

    /**
     *
     * @param urlPath
     * @return
     */
    private String buildFinalURL(String urlPath){
        Uri loadUri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority(BASE_URL)
                .path(urlPath)
                .build();
        return loadUri.toString();
    }
}