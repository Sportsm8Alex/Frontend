package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.DataBaseConnection.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.ApiServices.MeetingApiService;
import com.android.brogrammers.sportsm8.DataBaseConnection.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.MeetingsRepository;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Korbi on 14.06.2017.
 */

public class DatabaseMeetingsRepository implements MeetingsRepository {

    private MeetingApiService apiService;

    public DatabaseMeetingsRepository() {
        apiService = APIUtils.getMeetingAPIService();
    }

    @Override
    public Single<JsonObject> isMeetingReady(final Meeting meeting) {
        return apiService.isMeetingConfirmed(meeting.MeetingID);
    }

    @Override
    public Single<List<Meeting>> getMeetings(String email) {
        return apiService.getMeetings(email);
    }

}
