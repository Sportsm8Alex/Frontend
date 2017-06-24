package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

/**
 * Created by Korbi on 22.06.2017.
 */

public class Match implements Serializable {
    public int matchID,teamID_1,teamID_2,activity_ID,score_team_1,score_team_2;
    public String time,team1,team2;

    public DateTime getTime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
        return formatter.parseDateTime(time);
    }
}
