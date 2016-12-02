package com.example.alex.helloworld.databaseConnection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by agemcipe on 09.11.16.
 */

/**
 * parent class for all database connections
 */

//Benutz ich nicht

public class DatabaseConnection extends AsyncTask<String, Void, String>{
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static final String BASE_URL = "10.0.2.2";
    public static final String DEBUG_TAG = "some clever Debug tag:";

    private InputStream is;
    private HttpURLConnection conn;

    //this method will be implemented in the subclasses of DatabaseConnection
    @Override
    protected String doInBackground(String... params) {

        //depending on request use reader, writer with params
        return null;
    }

    /**
     * loadData method to download the data from the server that is requested via
     * http get
     *
     * @param params
     * @return InputStream
     * @throws IOException
     */
    private String read(String... params) throws IOException {
        //create the URL that will be requested from the Server
        Uri loadUri = new Uri.Builder()
                .scheme("http")
                //use encodedAuthority here because the BASE_URL is already encoded
                .encodedAuthority(BASE_URL)
                .path("IndexMeetings.html")
                //here we add the query parameters for our get request
                //question: HOW TO DO THIS DYNAMICALLY?!
                .build();

        for(int i = 0; i<(params.length/2); i+=2){
            loadUri.buildUpon().appendQueryParameter(params[i], params[i+1]);
        }

        try {
            //form URL
            Log.d(DEBUG_TAG, loadUri.toString());
            URL url = new URL(loadUri.toString());

            //request data from server via http get request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            //the response code from the server
            int response = conn.getResponseCode();
            Log.d("SOME DEBUG TAG", "The response is: "+ response);

            //the InputStream of the data send back from the server
            is = conn.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return inputReader(is);
    }

    /**
     * method to send Data to the database
     *
     * NOT IMPLEMENTED YET
     * @param args
     * @return a boolean value indicating success or not
     */
    protected boolean write(String... args){
        return false;
    }

    /**
     * method for converting an inputstream into a String
     * @param is the InputStream
     * @return String
     */
    private String inputReader(InputStream is) {
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
        System.out.println(successString.toString());

        return successString.toString();
    }
}