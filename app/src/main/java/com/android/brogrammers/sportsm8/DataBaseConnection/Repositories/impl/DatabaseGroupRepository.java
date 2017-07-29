package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.GroupRepository;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Korbi on 29.06.2017.
 */

public class DatabaseGroupRepository implements GroupRepository {

    private APIService apiService;

    public DatabaseGroupRepository() {
        this.apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<List<UserInfo>> getGroupMembers(final int groupID) {
        return Single.fromCallable(new Callable<List<UserInfo>>() {
            @Override
            public List<UserInfo> call() throws Exception {
                return apiService.getGroupMembers("getGroupMembers",groupID).execute().body();
            }
        });
    }

    @Override
    public Completable leaveGroup(final int groupID) {
        return apiService.leaveGroup("leaveGroup",groupID, LoginScreen.getRealEmail());
    }

    @Override
    public Single<ResponseBody> addMembersToGroup(final int groupID, final Map<String, String> newMembers) {
        return Single.fromCallable(new Callable<ResponseBody>() {
            @Override
            public ResponseBody call() throws Exception {
                return apiService.addMembersToGroup("adMembersToGroup",groupID,newMembers).execute().body();
            }
        });
    }
}
