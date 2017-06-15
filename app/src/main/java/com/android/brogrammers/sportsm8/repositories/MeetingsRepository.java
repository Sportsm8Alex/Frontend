package com.android.brogrammers.sportsm8.repositories;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Success;

import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Korbi on 14.06.2017.
 */

public interface MeetingsRepository {
    Single<ResponseBody> isMeetingReady(Meeting meeting);
}
