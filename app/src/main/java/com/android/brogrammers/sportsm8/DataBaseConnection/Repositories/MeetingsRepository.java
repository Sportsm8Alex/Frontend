package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Korbi on 14.06.2017.
 */

public interface MeetingsRepository {
    Single<JsonObject> isMeetingReady(Meeting meeting);
}
