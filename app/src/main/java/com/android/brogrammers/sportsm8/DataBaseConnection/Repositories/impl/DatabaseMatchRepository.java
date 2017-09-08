package com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl;

import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Match;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.MatchRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by Korbi on 22.06.2017.
 */

public class DatabaseMatchRepository implements MatchRepository {

    private APIService apiService;

    public DatabaseMatchRepository() {
        apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<List<Match>> getMatches() {
        return apiService.getFriendsMatches(LoginScreen.getRealEmail());
    }
}
