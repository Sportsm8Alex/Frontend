package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.TeamsRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by Korbi on 16.06.2017.
 */

public class DatabaseTeamsRepository implements TeamsRepository {

    private APIService apiService;

    public DatabaseTeamsRepository() {
        apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<List<Team>> getTeams() {
        return apiService.getTeams("getTeams", LoginScreen.getRealEmail());
    }

    @Override
    public Single<List<UserInfo>> getTeamMembers(final Team team) {
        return Single.fromCallable(new Callable<List<UserInfo>>() {
            @Override
            public List<UserInfo> call() throws Exception {
                return apiService.getTeamMembers("getTeamMembers", team.teamID).execute().body();
            }
        });
    }


}


