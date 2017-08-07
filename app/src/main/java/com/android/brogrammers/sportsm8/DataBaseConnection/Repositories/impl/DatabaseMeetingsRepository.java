package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.MeetingsRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Korbi on 14.06.2017.
 */

public class DatabaseMeetingsRepository implements MeetingsRepository {

    private APIService apiService;

    public DatabaseMeetingsRepository() {
        apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<JsonObject> isMeetingReady(final Meeting meeting) {
        return apiService.isMeetingConfirmed(meeting.MeetingID);
    }
}
