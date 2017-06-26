package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;

import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Korbi on 14.06.2017.
 */

public interface MeetingsRepository {
    Single<ResponseBody> isMeetingReady(Meeting meeting);
}
