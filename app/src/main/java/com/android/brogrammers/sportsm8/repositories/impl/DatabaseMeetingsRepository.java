package com.android.brogrammers.sportsm8.repositories.impl;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Success;
import com.android.brogrammers.sportsm8.repositories.MeetingsRepository;

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
