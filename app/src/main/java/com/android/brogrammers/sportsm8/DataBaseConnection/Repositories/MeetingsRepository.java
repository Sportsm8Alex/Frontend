package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories;

import com.android.brogrammers.sportsm8.DataBaseConnection.DatabaseClasses.Meeting;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Korbi on 14.06.2017.
 */

public interface MeetingsRepository {
    Single<JsonObject> isMeetingReady(Meeting meeting);
    Single<List<Meeting>> getMeetings(String email);
}
