package com.example.alex.helloworld.databaseConnection;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.alex.helloworld.Information;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alex on 11/18/2016.
 */

public class Database extends AsyncTask<String, String, String>{

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=10000;
    private UIthread uiThread;

    public Database(UIthread thread){
        uiThread = thread;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params){

        String success = "";
        HttpURLConnection conn;

        Uri.Builder builder = new Uri.Builder();
        for(int i=1; i<params.length; i+=2){
            builder.appendQueryParameter(params[i], params[i+1]);
        }
        String query = builder.build().getEncodedQuery();

        try {
            //http://10.0.2.2:8080/android_user_api/Backend/
            URL url = new URL("http://sportsm8.bplaced.net/MySQLadmin/include/"+params[0]);
            System.out.println(url.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout((READ_TIMEOUT));
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null){
                success += line;
                //System.out.println(line+"<--- line");
            }
            System.out.println(success);
            //JsonParser parser = new JsonParser();
            //JsonObject gson = (JsonObject) parser.parse(success);

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
    protected void onPostExecute(String success){
        try {
            uiThread.updateUI(jsonToArrayList(success));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Information> jsonToArrayList(String stringFromJson) throws JSONException, ParseException {

        ArrayList<Information> data = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(stringFromJson);

        int i=0;
        while(jsonObject.has(""+i)) {
            // in the php return JSON each meeting object is identified by an Index. Now they will be turned into a String one after another
            String meetingString = jsonObject.get(""+i).toString();
            Gson gson = new Gson();
            // current Meeting is created from meetingString according to requirements of Information.class
            Information currentMeeting = gson.fromJson(meetingString, Information.class);
            data.add(currentMeeting);
            i++;
        }

        return data;
    }
}
