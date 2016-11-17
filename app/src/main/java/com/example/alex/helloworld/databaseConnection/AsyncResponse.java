package com.example.alex.helloworld.databaseConnection;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

/**
 * Created by Korbi on 13.11.2016.
 */

public interface AsyncResponse {
    void processFinish(String output) throws ParseException, JSONException;
}
