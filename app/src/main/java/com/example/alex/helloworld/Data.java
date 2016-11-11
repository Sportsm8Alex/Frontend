package com.example.alex.helloworld;

/**
 * Created by alex on 10/30/2016.
 */

import java.util.ArrayList;

/**
 * Created by Korbi on 22.10.2016.
 */

public class Data {

    public static ArrayList<Information> getInvites() {

        ArrayList<Information> data = new ArrayList<>();


        String[] Categories = {"Beachvolleyball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball"};
        String[] Start = {"12:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00"};
        String[] Stop = {"11:00","12:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00"};
        String[] Datum = {"5.7","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4"};

        for (int i = 0; i < Categories.length; i++) {

            Information current = new Information();
            current.title = Categories[i];
            current.start = Start[i];
            current.stop = Stop[i];
            current.datum = Datum[i];

            data.add(current);
        }

        return data;
    }
    public static ArrayList<Information> getCalendar() {
        //get an arrayList from Loader.json

        ArrayList<Information> data = new ArrayList<>();

        String[] Categories = {"Beachvolleyball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball", "Laufen", "Beachvolleyball","Fußball"};
        String[] Start = {"12:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00","11:00","17:00","19:00"};
        String[] Stop = {"11:00","12:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00","19:00","17:00"};
        String[] Datum = {"5.7","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4","4.8","10.9","4.4"};

        for (int i = 0; i < Categories.length; i++) {

            Information current = new Information();
            current.title = Categories[i];
            current.start = Start[i];
            current.stop = Stop[i];
            current.datum = Datum[i];

            data.add(current);
        }

        return data;
    }
}