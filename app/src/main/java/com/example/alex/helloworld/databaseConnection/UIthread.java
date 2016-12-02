package com.example.alex.helloworld.databaseConnection;

import com.example.alex.helloworld.Information;

import java.util.ArrayList;

/**
 * Created by alex on 11/18/2016.
 */

public interface UIthread {
    void updateUI();
    //update the user interface according to the answer from the server
    void updateUI(String answer);
}
