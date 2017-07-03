package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public class DatabaseUserRepository implements UserRepository {

    private APIService apiService;

    public DatabaseUserRepository() {
        apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<List<UserInfo>> getUsers(final int meetingID) {
        return Single.fromCallable(new Callable<List<UserInfo>>() {
            @Override
            public List<UserInfo> call() throws Exception {
                return apiService.getMemberList("getMeetingMembers", meetingID).execute().body();
            }
        });
    }

    @Override
    public Single<ResponseBody> addUsersToMeeting(final int meetingID, final Map<String, String> membersMap) {
       return Single.fromCallable(new Callable<ResponseBody>() {
           @Override
           public ResponseBody call() throws Exception {
               return apiService.addMembersToMeeting2("addMembersToMeeting", meetingID, membersMap).execute().body();
           }
       });
    }
////////////////////////////////////////////////////
    @Override
    public Single<ResponseBody> confirmMeeting(final Meeting meeting) {
        if (meeting.dynamic == 0) {
            return Single.fromCallable(new Callable<ResponseBody>() {
                @Override
                public ResponseBody call() throws Exception {
                    return apiService.confirmMeeting2("confirmAtt", meeting.MeetingID, LoginScreen.getRealEmail()).execute().body();
                }
            });
        } else {
          return null;
        }

    }
}

