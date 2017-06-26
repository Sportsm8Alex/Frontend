package com.android.brogrammers.sportsm8.DataBaseConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korbi on 02.06.2017.
 */

public class DatabaseHelperMeetings implements Callback {
    private APIService apiService = APIUtils.getAPIService();
    private String email;

    public DatabaseHelperMeetings(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
        email = sharedPrefs.getString("email", "");
    }

    public void confirm(Meeting meetingConfirm) {
        if (meetingConfirm.dynamic == 0) {
            apiService.confirmMeeting("confirmAtt", meetingConfirm.MeetingID, email).enqueue(this);
        } else {
            DateTime timeS = meetingConfirm.getStartDateTime();
            DateTime timeE = meetingConfirm.getEndDateTime();
            //  setOtherTime2(timeS,timeE,meetingConfirm);
            // setOtherTime(timeS.getHourOfDay(), timeS.getMinuteOfHour(), timeE.getHourOfDay(), timeE.getMinuteOfHour(), meetingConfirm);
        }
    }

    public void confirm2(View view, Meeting meetingConfirm) {
        if (meetingConfirm.dynamic == 0) {
            apiService.confirmMeeting("confirmAtt", meetingConfirm.MeetingID, email).enqueue(this);
        } else {
            DateTime timeS = meetingConfirm.getStartDateTime();
            DateTime timeE = meetingConfirm.getEndDateTime();
            //setOtherTime2(timeS,timeE,meetingConfirm);
            // setOtherTime(timeS.getHourOfDay(), timeS.getMinuteOfHour(), timeE.getHourOfDay(), timeE.getMinuteOfHour(), meetingConfirm);
        }
    }

    public void setOtherTime2(DateTime start, DateTime end, Meeting meeting) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-YYYY HH:mm:ss");
        apiService.setOtherTime2("setOtherTime", formatter.print(start),formatter.print(end), meeting.MeetingID, email).enqueue(this);
    }


    public void setOtherTime(int begin, int beginMinute, int end, int endMinute, Meeting meeting) {
        int beginDatabase = begin * 4 + (beginMinute / 15);
        int durationDatabase = ((end * 4) + endMinute / 15) - ((begin * 4) + beginMinute / 15);
        apiService.setOtherTime("setOtherTime", beginDatabase, durationDatabase, meeting.MeetingID, email).enqueue(this);
    }

    public void declineMeeting(Meeting infoData) {
        apiService.declineMeeting("declineAtt", infoData.MeetingID, email).enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {
    }

    @Override
    public void onFailure(Call call, Throwable t) {
    }
}
