package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.MeetingsRepository;

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
    public Single<ResponseBody> isMeetingReady(final Meeting meeting) {
        return Single.fromCallable(new Callable<ResponseBody>() {
            @Override
            public ResponseBody call() throws Exception {
                return apiService.isMeetingConfirmed("isMeetingConfirmed",meeting.MeetingID).execute().body();
            }
        });
    }
}
