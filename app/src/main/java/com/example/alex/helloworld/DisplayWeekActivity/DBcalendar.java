package com.example.alex.helloworld.DisplayWeekActivity;

import android.net.Uri;
import android.util.Log;

import com.example.alex.helloworld.databaseConnection.DBconnection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alex on 11/11/2016.
 */

//Benutz ich auch nicht

public class DBcalendar{

}

/*public class DBcalendar extends DBconnection {
    /*public String readCalendar() {
        super.read();
    }

    @Override
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... params){
        Uri loadUri = new Uri.Builder()
                .scheme("http")
                //use encodedAuthority here because the BASE_URL is already encoded
                .encodedAuthority(BASE_URL)
                .path("IndexMeetings.html")
                //here we add the query parameters for our get request
                //question: HOW TO DO THIS DYNAMICALLY?!
                .build();
        for (int i = 0; i < (params.length / 2); i += 2) {
            loadUri.buildUpon().appendQueryParameter(params[i], params[i + 1]);
        }
        Log.d(DEBUG_TAG, loadUri.toString());
        URL url = null;
        try {
            url = new URL(loadUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        super.getConnection(url);
        return null;
    }

    @Override
    protected void onPostExecute(String success){
    }
}
*/