package com.android.brogrammers.sportsm8.repositories;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Korbi on 07.06.2017.
 */

public interface UserRepository {
   Single<List<UserInfo>> getUsers(int meetingID);
   Single<ResponseBody> addUsersToMeeting(int meetingID, Map<String, String> membersMap);
   Single<ResponseBody> confirmMeeting(Meeting meeting);
}
