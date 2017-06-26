package com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Korbi on 25.06.2017.
 */

public class TimeCalcObject {
    public DateTime time;
    public int minusOrPlus;
    public int number=1;
    DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");

    public TimeCalcObject(String time, int minusOrPlus) {
        this.time = formatter.parseDateTime(time);
        this.minusOrPlus = minusOrPlus;
    }
}
